package models


import javax.inject.Inject


import anorm.SqlParser._
import anorm._


import play.api.db.DBApi


case class Airport(id: Option[Int] = None,
                   ident: String,
                   type1: Option[String],
                   name: Option[String],
                   latitude_deg: Option[Double],
                   longitude_deg: Option[Double],
                   elevation_ft: Option[Int],
                   continent: Option[String],
                   iso_country: Option[String],
                   iso_region: Option[String],
                   municipality: Option[String],
                   scheduled_service: Option[String],
                   gps_code: Option[String],
                   iata_code: Option[String],
                   local_code: Option[String],
                   home_link: Option[String],
                   wikipedia_link: Option[String],
                   keywords: Option[String]
                  )


@javax.inject.Singleton
class AirportsService @Inject()(dbapi: DBApi) {

  private val db = dbapi.database("default")

  val simple = {
    get[Option[Int]]("airports.id") ~
      get[String]("airports.ident") ~
      get[Option[String]]("airports.type1") ~
      get[Option[String]]("airports.name") ~
      get[Option[Double]]("airports.latitude_deg") ~
      get[Option[Double]]("airports.longitude_deg") ~
      get[Option[Int]]("airports.elevation_ft") ~
      get[Option[String]]("airports.continent") ~
      get[Option[String]]("airports.iso_country") ~
      get[Option[String]]("airports.iso_region") ~
      get[Option[String]]("airports.municipality") ~
      get[Option[String]]("airports.scheduled_service") ~
      get[Option[String]]("airports.gps_code") ~
      get[Option[String]]("airports.iata_code") ~
      get[Option[String]]("airports.local_code") ~
      get[Option[String]]("airports.home_link") ~
      get[Option[String]]("airports.wikipedia_link") ~
      get[Option[String]]("airports.keywords") map {
      case id ~ ident ~ type1 ~ name ~ latitude_deg ~ longitude_deg ~ elevation_ft ~ continent ~ iso_country ~ iso_region ~ municipality ~ scheduled_service ~ gps_code ~ iata_code ~ local_code ~ home_link ~ wikipedia_link ~ keywords =>
        Airport(id, ident, type1, name, latitude_deg, longitude_deg, elevation_ft, continent, iso_country, iso_region, municipality, scheduled_service, gps_code, iata_code, local_code, home_link, wikipedia_link, keywords)
    }
  }
}