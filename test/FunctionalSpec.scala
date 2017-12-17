
import controllers.SearchController
import org.scalatest.concurrent.ScalaFutures
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._

class FunctionalSpec extends PlaySpec with OneAppPerSuite with ScalaFutures {

  def searchController = app.injector.instanceOf(classOf[SearchController])

  "searchController" should {

    "redirect to the airports list on /" in {
      val result = searchController.index(FakeRequest())

      status(result) must equal(SEE_OTHER)
      redirectLocation(result) mustBe Some("/search")
    }
  }
}