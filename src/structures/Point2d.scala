package structures

/** An immutable double-precision 2-d Cartesian point.
 * 
 * @constructor create a new point at the given coordinates
 * @param x the x coordinate
 * @param y the y coordinate
 */
class Point2d(val x: Double, val y: Double) {

  /** Creates a point at the origin. */
  def this() = this(0.0, 0.0)

  /** Creates a point given by the input point. */
  def this(p: java.awt.Point) = this(p.x, p.y)
  
  /** Creates a point as determined by a vector. */
  def this(v: Vector2d) = this(v.xComponent, v.yComponent)

  /** Rounds the point to an integer-precision point. */
  def getIntegerPoint(): java.awt.Point = {
    def round(x: Double): Int = math.round(x.asInstanceOf[Float])
    new java.awt.Point(round(x), round(y))
  }
  
  /** Gives the square of the distance between two points. */
  def distanceSquared(p: Point2d): Double = {
    val dx = x - p.x
    val dy = y - p.y
    dx * dx + dy * dy
  }

  /** Gives the distance between two points. */
  def distance(p: Point2d): Double = math.sqrt(distanceSquared(p))

  /** Tells if this point is within a certain maximum distance from another point.*/
  def withinDistance(p: Point2d, max: Double): Boolean =
    distanceSquared(p) <= max * max

  /** Translates a point by dx and dy. */
  def translate(dx: Double, dy: Double): Point2d = new Point2d(x + dx, y + dy)

  /** Translates a point by dx and dy. */
  def translate(p: Point2d): Point2d = translate(p.x, p.y)

  /** Translates a point by dx and dy. */
  def +(dx: Double, dy: Double): Point2d = translate(dx, dy)

  /** Translates a point by the values of another point */
  def +(p: Point2d): Point2d = translate(p.x, p.y)

  /** Translates a point by -dx and -dy. */
  def -(dx: Double, dy: Double): Point2d = translate(-dx, -dy)

  /** Translates a point by the negation of another point */
  def -(p: Point2d): Point2d = this - (p.x, p.y)

  /** Tells if another instance can equal a Point2d. */
  def canEqual(other: Any): Boolean = other.isInstanceOf[structures.Point2d]  

  override def equals(other: Any): Boolean = {
    other match {
      case that: structures.Point2d => 
        that.canEqual(Point2d.this) &&
        x == that.x && y == that.y
      case _ => false
    }
  }
  
  /** Tells if two types are equal, with epsilon error allowed. */
  def equals(other: Any, epsilon: Double): Boolean = {
    def eq(x: Double, y: Double) = math.abs(x - y) <= epsilon
    other match {
      case that: structures.Point2d => 
        that.canEqual(Point2d.this) &&
        eq(x, that.x) &&
        eq(y, that.y)
      case _ => false
    }
  }
  
  override def hashCode(): Int = {
    val prime = 41
    prime * (prime + x.hashCode) + y.hashCode
  }

  override def toString(): String = "(" + x + ", " + y + ")";

}