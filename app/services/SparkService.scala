package services

import models.{Rating, Movie, User}
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD

import play.Configuration

object SparkService {
  private lazy val context = {
    val conf = new SparkConf(false)
      .setAppName(SparkConfig.appName)
      .setMaster(SparkConfig.master)

      for((key,value) <- SparkConfig.configs) {
        conf.set(key, value)
      }

    new SparkContext(conf)
  }

  lazy val userRDD = {
    val input: RDD[String] = context.textFile(Configuration.root().getString("user.file"))
    val users = input.map(User.fromString)
    users.cache()
    users
  }

  lazy val movieRDD = {
    val input: RDD[String] = context.textFile(Configuration.root().getString("movie.file"))
    val movies = input.map(Movie.fromString)
    movies.cache()
    movies
  }

  lazy val ratingRDD = {
    val input: RDD[String] = context.textFile(Configuration.root().getString("data.file"))
    val ratings = input.map(Rating.fromString)
    ratings.cache()
    ratings
  }

  def version = context.version
}
