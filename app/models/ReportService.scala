package models

import javax.inject.Inject

import anorm.SqlParser.{get, scalar}
import anorm.{SQL, ~}
import play.api.db.DBApi


case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}

@javax.inject.Singleton
class ReportService @Inject()(dbapi: DBApi, countriesService: CountriesService, airportsService: AirportsService, runwaysService: RunwaysService) {

  private val db = dbapi.database("default")


  val reportCountryAirportRunway = (countriesService.simple ?) ~ (airportsService.simple ?) ~ (runwaysService.simple ?) map {
    case country ~ airport ~ runway => (country, airport, runway)
  }

  val reportCountryAirportCount = (countriesService.simple ?) ~ (airportsService.simple ?) ~ get[Int]("air") map {
    case country ~ airport ~ air => (country, airport, air)
  }

  val reportRunwayidCount = (runwaysService.simple2 ?) ~ get[Int]("air") map {
    case runway ~ air => (runway, air)
  }


  val reportCountryRunwayTypes = (countriesService.simple ?) ~ (runwaysService.simple3 ?) map {
    case country ~ runway => (country, runway)
  }


  def list(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Page[(Option[Country], Option[Airport], Option[Runway])] = {

    val offset = pageSize * page

    db.withConnection { implicit connection =>

      val resultSet = SQL(
        """
          SELECT cr.*, ar.*, rn.* FROM countries as cr
          LEFT JOIN airports AS ar
          ON cr.code = ar.iso_country
          LEFT JOIN runways as rn
          ON ar.id = rn.airport_ref
          WHERE cr.name like {filter} OR cr.code like {filter}
          ORDER BY {orderBy} nulls last
          LIMIT {pageSize} offset {offset}
        """
      ).on(
        'pageSize -> pageSize,
        'offset -> offset,
        'filter -> filter,
        'orderBy -> orderBy
      ).as(reportCountryAirportRunway *)

      val totalRows = SQL(
        """
           SELECT count(*) FROM runways
           WHERE runways.airport_ref in
           (SELECT airports.id FROM airports
           WHERE airports.iso_country IN (
           SELECT distinct (cr.code) FROM countries AS cr
              LEFT JOIN airports AS ar
              ON cr.code = ar.iso_country
              LEFT JOIN runways AS rn
              ON ar.id = rn.airport_ref
              WHERE cr.name like {filter} OR cr.code like {filter}
           ))
        """
      ).on(
        'filter -> filter
      ).as(scalar[Int].single)

      Page(resultSet, page, offset, totalRows)
    }
  }

  def listASC(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1): Page[(Option[Country], Option[Airport], Int)] = {

    val offset = pageSize * page

    db.withConnection { implicit connection =>

      val resultSet = SQL(
        """
        SELECT TOP 10 countries.*, COUNT(airports.id) AS air
        FROM countries
        LEFT JOIN airports
        ON countries.code = airports.iso_country
        GROUP BY countries.id
        ORDER BY air ASC
        """
      ).on(
        'pageSize -> pageSize,
        'offset -> offset
      ).as(reportCountryAirportCount *)

      Page(resultSet, page, offset, 10)
    }
  }

  def listDESC(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1): Page[(Option[Country], Option[Airport], Int)] = {

    val offset = pageSize * page

    db.withConnection { implicit connection =>

      val resultSet = SQL(
        """
          SELECT TOP 10 countries.*, COUNT(airports.id) AS air
          FROM airports, countries
          WHERE countries.code = airports.iso_country
          GROUP BY ISO_COUNTRY
          ORDER BY air DESC
        """
      ).on(
        'pageSize -> pageSize,
        'offset -> offset
      ).as(reportCountryAirportCount *)

      Page(resultSet, page, offset, 10)
    }
  }

  def listCommon(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1): Page[(Option[ReportCommon], Int)] = {

    val offset = pageSize * page

    db.withConnection { implicit connection =>

      val resultSet = SQL(
        """
          SELECT TOP 10 runways.le_ident, count(runways.le_ident) as air
          FROM runways where runways.le_ident IS NOT NULL
          GROUP BY le_ident
          ORDER BY air DESC
        """
      ).on(
        'pageSize -> pageSize,
        'offset -> offset
      ).as(reportRunwayidCount *)

      Page(resultSet, page, offset, 10)
    }
  }

  def listRwTypesByCountry(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1): Page[(Option[Country], Option[ReportRunwaysByCountry])] = {

    val offset = pageSize * page

    db.withConnection { implicit connection =>

      val resultSet = SQL(
        """
          SELECT countries.name, GROUP_CONCAT(DISTINCT runways.surface SEPARATOR ', ') as rwtypes
          FROM countries,runways WHERE substring(runways.airport_ident,1,2) =countries.code
          GROUP BY countries.name
          order by name nulls last
          limit {pageSize} offset {offset}
        """
      ).on(
        'pageSize -> pageSize,
        'offset -> offset
      ).as(reportCountryRunwayTypes *)

      val totalRows = SQL(
        """
           select count(*) from countries
        """
      ).as(scalar[Int].single)

      Page(resultSet, page, offset, totalRows)
    }
  }

}