import org.apache.spark.sql.SparkSession
import scala.collection.immutable.List

object SpatialQuery extends App{
  def runRangeQuery(spark: SparkSession, arg1: String, arg2: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    spark.udf.register("ST_Contains",(queryRectangle:String, pointString:String)=>(ST_Contains(queryRectangle, pointString)))

    val resultDf = spark.sql("select * from point where ST_Contains('"+arg2+"',point._c0)")
    resultDf.show()

    return resultDf.count()
  }

  def runRangeJoinQuery(spark: SparkSession, arg1: String, arg2: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    val rectangleDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg2);
    rectangleDf.createOrReplaceTempView("rectangle")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    spark.udf.register("ST_Contains",(queryRectangle:String, pointString:String)=>(ST_Contains(queryRectangle, pointString)))

    val resultDf = spark.sql("select * from rectangle,point where ST_Contains(rectangle._c0,point._c0)")
    resultDf.show()

    return resultDf.count()
  }

  def runDistanceQuery(spark: SparkSession, arg1: String, arg2: String, arg3: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    spark.udf.register("ST_Within",(pointString1:String, pointString2:String, distance:Double)=>(ST_Within(pointString1, pointString2, distance)))

    val resultDf = spark.sql("select * from point where ST_Within(point._c0,'"+arg2+"',"+arg3+")")
    resultDf.show()

    return resultDf.count()
  }

  def runDistanceJoinQuery(spark: SparkSession, arg1: String, arg2: String, arg3: String): Long = {

    val pointDf = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg1);
    pointDf.createOrReplaceTempView("point1")

    val pointDf2 = spark.read.format("com.databricks.spark.csv").option("delimiter","\t").option("header","false").load(arg2);
    pointDf2.createOrReplaceTempView("point2")

    // YOU NEED TO FILL IN THIS USER DEFINED FUNCTION
    spark.udf.register("ST_Within",(pointString1:String, pointString2:String, distance:Double)=>(ST_Within(pointString1, pointString2, distance)))
    val resultDf = spark.sql("select * from point1 p1, point2 p2 where ST_Within(p1._c0, p2._c0, "+arg3+")")
    resultDf.show()

    return resultDf.count()
  }

  /**
    * ST_Contains function: When whether point is with in the rectangle
    * @param queryRectangle
    * @param pointString
    * @return Boolean
    */


  def ST_Contains(queryRectangle:String, pointString:String): Boolean ={

    val queryRectangleSplit:Array[String] = queryRectangle.trim().split(",")
    val pointStringSplit:Array[String] = pointString.trim().split(",")
    var rectangle_x:List[Double] = List()
    var rectangle_y:List[Double] = List()
    var points_x:Double = 0
    var points_y:Double = 0

    var count = 0

    queryRectangleSplit.foreach { x => {
      if(count%2==0){
      rectangle_x = rectangle_x :+ x.toDouble
    }
      else{
        rectangle_y = rectangle_y :+ x.toDouble
      }
      count += 1
    }
    }

    count = 0

    pointStringSplit.foreach { x => {
      if(count%2==0){
        points_x = x.toDouble
      }
      else{
        points_y =  x.toDouble
      }
      count += 1
    }
    }

    rectangle_x = rectangle_x.sorted
    rectangle_y = rectangle_y.sorted

    if(points_x>=rectangle_x(0) && points_x<=rectangle_x(1) && points_y>=rectangle_y(0) && points_y<=rectangle_y(1)){
      return true
    }
    else{
      return false
    }

  }

  /**
    * ST_Within function check whether the distance between the two points is less than distance
    * @param pointString1
    * @param pointString2
    * @param distance
    * @return Boolean
    */

  def ST_Within(pointString1:String, pointString2:String, distance:Double): Boolean ={

    def stToPoint(point:String):List[Double]= {
      val pointStringSplit: Array[String] = point.trim ().split (",")
      var points: List[Double] = List ()
      var count = 0
      pointStringSplit.foreach { x => {
        points = points :+ x.toDouble
      }
      }
      return points
    }

    val p1= stToPoint(pointString1)
    val p2= stToPoint(pointString2)
    val euclidean_distance = scala.math.pow(scala.math.pow((p1(0) - p2(0)), 2) + scala.math.pow((p1(1) - p2(1)), 2), 0.5)

    return euclidean_distance <= distance


    }


}
