package controllers

import models.Recommendation
import serializers.RecommendationSerializer._
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.recommend.{RecommendService, SimilarityService}

import javax.inject.Inject

class RecommendController @Inject()(cc: ControllerComponents, config: Configuration) extends AbstractController(cc) {

  def test():Action[AnyContent] = Action { implicit request =>
    println(s"환경변수 좀 출력해봐: ${config.get[String]("mongo.hostname")}")
    Ok("Test API is working!!!")
  }

  def recommend(): Action[AnyContent] = Action { implicit request =>
    val list: List[Recommendation] = RecommendService.recommend()
    Ok(Json.toJson(list))
  }

  def userSimilarity(): Action[AnyContent] = Action { implicit request =>
    val str = SimilarityService.calculateUserSimilarity()
    Ok("Similarity API is working! " + str)
  }

  def recruitSimilarity(): Action[AnyContent] = Action { implicit request =>
    val str = SimilarityService.calculateRecruitSimilarity()
    Ok("Recruit Similarity API is working! " + str)
  }
}
