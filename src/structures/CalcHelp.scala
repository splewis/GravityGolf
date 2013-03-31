package structures

import java.awt.Color
import java.util.Random

/**
 * Class that provides several frequently used methods across classes
 * @author Sean Lewis
 */
object CalcHelp {

  private val randomGenerator = new Random

  /**
   * Rounds a Double precision value to an Integer.
   */
  def round(x: Double): Int 
    = math.round(x.asInstanceOf[Float])

  /**
   * Creates a new color based on the specified component values. If the
   * values are out of the appropriate color bounds, they are adjusted to fit
   * in. (-10 becomes 0, 260 becomes 255, etc.)
   * @param r the red value
   * @param g the green value
   * @param b the blue value
   * @return a new Color
   */
  def correctColor(r: Int, g: Int, b: Int): Color 
    = new Color(bound(r, 0, 255), bound(g, 0, 255), bound(b, 0, 255))
  

  /**
   * Returns a random (with normal distribution) double.
   * @param mean the mean of the distribution
   * @param standardDeviation the standard deviation of the distribution
   * @return a random double form a normal distribution
   */
  def gaussianDouble(mean: Double, standardDeviation: Double): Double 
    = randomGenerator.nextGaussian() * standardDeviation + mean

  /**
   * Bounds a value to be within the given range.
   * @param min the smallest allowed value
   * @param max the largest allowed value
   */
  def bound[T <% Ordered[T]](x: T, min: T, max: T): T = {
    if (x < min) min
    if (x > max) max
    else x
  }

  /**
   * Returns a random (with normal distribution) double that is guaranteed to
   * lie between two values.
   * @param mean the mean of the distribution
   * @param standardDeviation the standard deviation of the distribution
   * @param min the smallest value that may be returned
   * @param max the largest value that may be returned
   * @return a random double form a normal distribution
   */
  def gaussianDoubleInRange(mean: Double,
    standardDeviation: Double, min: Double, max: Double): Double = {
    if (min > max)
      throw new IllegalArgumentException("The min parameter "
        + "must be less than or equal to the max parameter.");
    val rand = gaussianDouble(mean, standardDeviation);
    bound(rand, min, max)
  }

  /**
   * Returns a random (with normal distribution) int.
   * @param mean the mean of the distribution
   * @param standardDeviation the standard deviation of the distribution
   * @return a random int form a normal distribution
   */
  def gaussianInteger(mean: Double, standardDeviation: Double): Int 
    = round(gaussianDouble(mean, standardDeviation));

  /**
   * Returns a random (with normal distribution) double that is guaranteed to
   * lie between two values.
   * @param mean the mean of the distribution
   * @param standardDeviation the standard deviation of the distribution
   * @param min the smallest value that may be returned
   * @param max the largest value that may be returned
   * @return a random double form a normal distribution
   */
  def gaussianIntegerInRange(mean: Double,
    standardDeviation: Double, min: Int, max: Int): Int = {
    if (min > max)
      throw new IllegalArgumentException("The min parameter "
        + "must be less than or equal to the max parameter.");
    val rand = gaussianInteger(mean, standardDeviation);
    if (rand < min)
      min
    if (rand > max)
      max
    else rand;
  }

  /**
   * Calculates what quadrant an angle (in radians) is in. Uses the following
   * conventions: <li>[0, pi/2) - quadrant I <li>[pi/2, pi) - quadrant II <li>
   * [pi, 3pi/2) - quadrant III <li>[3pi/2, 2pi) - quadrant IV
   * <p>
   * For angles outside of [0, 2pi), equivalent coterminal angles are used.
   * @param angle an angle, in radians
   * @return the quadrant of angle
   */
  def quadrant(angle: Double): Int = {
    var x = angle
    while (x < 0)
      x += 2 * math.Pi;
    while (x > 2 * math.Pi)
      x -= 2 * math.Pi;

    if (x < math.Pi / 2)
      return 1;
    if (x < math.Pi)
      return 2;
    if (x < 3 * math.Pi / 2)
      return 3;
    return 4;
  }

  /**
   * Returns the gravitational acceleration experienced by an object.
   * @param location the location of the object experiencing the acceleration
   * @param sourceLocation the location of where the source of gravity comes
   *        from
   * @param strength the strength (typically the mass) of the source
   * @param g the gravitational constant
   * @return the magnitude of the acceleration felt
   */
  def getAcceleration(location: Point2d, sourceLocation: Point2d,
                      strength: Double, g: Double): Double 
    = return g * strength / location.distanceSquared(sourceLocation)

  /**
   * Calculates the angle (in radians), formed by (0,0) and (xDif, yDif).
   * @param xDif the x value
   * @param yDif the y value
   * @return the angle, in the range [0, 2pi)
   */
  def getAngle(xDif: Double, yDif: Double): Double = {
    val angle = java.lang.Math.atan(-yDif / xDif)
    if (xDif > 0.0)
      angle
    else if (xDif < 0.0)
      angle + math.Pi
    else if (yDif < 0.0)
      math.Pi / 2
    else if (yDif > 0.0)
      -math.Pi / 2
    else
      0.0
  }

  /**
   * Calculates the angle formed between two points. The angle is measured
   * from the first point to the second, against the horizontal line on which
   * the first point sits, measured from the right and counter-clockwise.
   * @param p1 the first point
   * @param p2 the second point
   * @return the angle, in the the range [0, 2pi)
   */
  def getAngle(p1: Point2d, p2: Point2d): Double 
    = getAngle(p2.x - p1.x, p2.y - p1.y)

  /**
   * Returns a new Color with each component randomly shifted by at most some
   * value. The values are adjusted so that the new Color is valid.
   * @param color the base color
   * @param maxDifference the maximum change for any one color
   * @return a color randomly shifted by at most maxDifference
   */
  def getShiftedColor(color: Color, maxDifference: Int): Color = {
    if (color == null)
      throw new NullPointerException("A null color cannot be shifted")
    var r = randomInteger(color.getRed() - maxDifference, color.getRed()
      + maxDifference)
    if (r < 0 || r > 255)
      r = color.getRed()

    var g = randomInteger(color.getGreen() - maxDifference,
      color.getGreen() + maxDifference)
    if (g < 0 || g > 255)
      g = color.getGreen()

    var b = randomInteger(color.getBlue() - maxDifference, color.getBlue()
      + maxDifference)
    if (b < 0 || b > 255)
      b = color.getBlue()

    return new Color(r, g, b)
  }

  /**
   * Randomly calculates a double in the range [a, b) with a uniform random
   * distribution.
   * @param a the minimum value (inclusive)
   * @param b the maximum value (exclusive)
   * @return random double in the range
   */
  def randomDouble(a: Double, b: Double): Double 
    = math.random * (b - a) + a;

  /**
   * Randomly calculates an int in the range [a, b) with a uniform random
   * distribution.
   * @param a the minimum value (inclusive)
   * @param b the maximum value (exclusive)
   * @return random int in the range
   */
  def randomInteger(a: Int, b: Int): Int 
    = round(randomDouble(a, b))

  /**
   * Randomly returns -1 or 1.
   * @return -1 or 1, randomly
   */
  def randomSign(): Int = {
    if (math.random < 0.5) -1
    else 1
  }

}