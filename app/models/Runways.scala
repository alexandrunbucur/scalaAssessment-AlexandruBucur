

/*
 * Copyright (c) 2017. Alexandru-Nicolae Bucur
 */

package models

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.DBApi

case class Runway(
                   id: Int,
                   airport_ref: Int,
                   airport_ident: Option[String],
                   length_ft: Option[Int],
                   width_ft: Option[Int],
                   surface: Option[String],
                   lighted: Option[Int],
                   closed: Option[Int],
                   le_ident: Option[String],
                   le_latitude_deg: Option[Double],
                   le_longitude_deg: Option[Double],
                   le_elevation_ft: Option[Double],
                   le_heading_degT: Option[Double],
                   le_displaced_threshold_ft: Option[Int],
                   he_ident: Option[String],
                   he_latitude_deg: Option[Double],
                   he_longitude_deg: Option[Double],
                   he_elevation_ft: Option[Double],
                   he_heading_degT: Option[Double],
                   he_displaced_threshold_ft: Option[Int]
                 )

case class ReportCommon(
                         le_ident: Option[String]
                       )

case class ReportRunwaysByCountry(
                                   cname: Option[String],
                                   rwtypes: Option[String]
                                 )

@javax.inject.Singleton
class RunwaysService @Inject()(dbapi: DBApi) {


  val simple = {
    get[Int]("runways.id") ~
      get[Int]("runways.airport_ref") ~
      get[Option[String]]("runways.airport_ident") ~
      get[Option[Int]]("runways.length_ft") ~
      get[Option[Int]]("runways.width_ft") ~
      get[Option[String]]("runways.surface") ~
      get[Option[Int]]("runways.lighted") ~
      get[Option[Int]]("runways.closed") ~
      get[Option[String]]("runways.le_ident") ~
      get[Option[Double]]("runways.le_latitude_deg") ~
      get[Option[Double]]("runways.le_longitude_deg") ~
      get[Option[Double]]("runways.le_elevation_ft") ~
      get[Option[Double]]("runways.le_heading_degT") ~
      get[Option[Int]]("runways.le_displaced_threshold_ft") ~
      get[Option[String]]("runways.he_ident") ~
      get[Option[Double]]("runways.he_latitude_deg") ~
      get[Option[Double]]("runways.he_longitude_deg") ~
      get[Option[Double]]("runways.he_elevation_ft") ~
      get[Option[Double]]("runways.he_heading_degT") ~
      get[Option[Int]]("runways.he_displaced_threshold_ft") map {
      case id ~ airport_ref ~ airport_ident ~ length_ft ~ width_ft ~ surface ~ lighted ~ closed ~ le_ident ~ le_latitude_deg ~ le_longitude_deg ~ le_elevation_ft ~ le_heading_degT ~ le_displaced_threshold_ft ~ he_ident ~ he_latitude_deg ~ he_longitude_deg ~ he_elevation_ft ~ he_heading_degT ~ he_displaced_threshold_ft
      =>
        Runway(id, airport_ref, airport_ident, length_ft, width_ft, surface, lighted, closed, le_ident, le_latitude_deg, le_longitude_deg, le_elevation_ft, le_heading_degT, le_displaced_threshold_ft, he_ident, he_latitude_deg, he_longitude_deg, he_elevation_ft, he_heading_degT, he_displaced_threshold_ft
        )
    }
  }
  val simple2 = get[Option[String]]("runways.le_ident") map {
    case le_ident => ReportCommon(le_ident)
  }
  val simple3 = get[Option[String]]("countries.name") ~
    get[Option[String]]("rwtypes") map {
    case cname ~ rwtypes => ReportRunwaysByCountry(cname, rwtypes)
  }

}