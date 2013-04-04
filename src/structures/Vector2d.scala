package structures

/** Representation of a two dimensional mathematical vector. Supports typical
 * operations on vectors in an immutable fashion. <li>Note that the angle of a
 * vector is defined as being measured in the counter-clockwise direction on the
 * x-axis. <li>Angles are always stored in radians.
 */
class Vector2d(val xComponent: Double, val yComponent: Double) {

  /** The norm of the vector. */
  val magnitude = math.hypot(xComponent, yComponent)
  /** The angle formed by the vector with the positive x axis.*/
  val angle = CalcHelp.getAngle(xComponent, yComponent)

  /** Creates a new zero vector. */
  def this() = this(0.0, 0.0)
  
  /** Creates a vector as determined by a point. */
  def this(p: Point2d) = this(p.x, p.y)

  /** Defines a vector in polar coordinates. */
  def polarVector(magnitude: Double, angle: Double) =
    new Vector2d(magnitude * math.cos(angle), magnitude * math.sin(angle))

  /** Negates a vector. */
  def unary_- = new Vector2d(-xComponent, -yComponent)

  /** Returns the sum of two vectors. */
  def add(v: Vector2d): Vector2d = 
    new Vector2d(xComponent + v.xComponent, yComponent + v.yComponent)

  /** Returns the sum of two vectors. */
  def +(v: Vector2d): Vector2d = this.add(v)

  /** Returns the difference of two vectors. */
  def subtract(v: Vector2d): Vector2d = this + (-v)

  /** Returns the difference of two vectors. */
  def -(v: Vector2d): Vector2d = this.subtract(v)
     
  /** Returns the scalar multiple of this vector by k. */ 
  def multiply(k: Double): Vector2d =
    new Vector2d(k * xComponent, k * yComponent)

  /** Returns the scalar multiple of this vector by 1/k. */
  def divide(k: Double): Vector2d = this.multiply(1.0 / k)

  /** Returns the scalar multiple of this vector by k. */
  def *(k: Double): Vector2d = this.multiply(k)

  /** Returns the scalar multiple of this vector by 1/k. */
  def /(k: Double): Vector2d = this.multiply(1.0 / k)

  /** Returns the dot product of two vectors. */
  def dot(v: Vector2d): Double =
    xComponent * v.xComponent + yComponent * v.yComponent

  /** Returns a unit vector in the direction of this vector. */
  def normalize(): Vector2d = this / this.dot(this)

  /** Returns the projection of this vector onto v. */
  def proj(v: Vector2d): Vector2d = v * (this.dot(v) / v.dot(v))
    
  /** Returns the projection of this vector in the direction of angle. */
  def proj(angle: Double): Vector2d
    = polarVector(magnitude * math.cos(this.angle - angle), angle)
  
  /** Returns if another type can equal this Vector2d. */
  def canEqual(other: Any): Boolean = 
    other.isInstanceOf[structures.Vector2d]  
  
  override def equals(other: Any): Boolean = {
    other match {
      case that: structures.Vector2d => 
        that.canEqual(Vector2d.this) && 
        xComponent == that.xComponent &&
        yComponent == that.yComponent
      case _ => false
    }
  }

  /** Returns if another instance can equal this Vector to epsilon precision. */
  def equals(other: Any, epsilon: Double): Boolean = {
    def eq(x: Double, y: Double) = math.abs(x - y) <= epsilon
    other match {
      case that: structures.Vector2d =>
        that.canEqual(Vector2d.this) &&
          eq(xComponent, that.xComponent) &&
          eq(yComponent, that.yComponent)
      case _ => false
    }
  }
  
  override def hashCode(): Int = {
    val prime = 41
    prime * (prime + xComponent.hashCode) + yComponent.hashCode
  }

  override def toString(): String = {
    "<" + xComponent + "i" +
      (if (yComponent >= 0) (" + ") else (" - ")) +
      math.abs(yComponent) + "j>"
  }
  
}