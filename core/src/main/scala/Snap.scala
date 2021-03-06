package akauppi.scalajs.snapsvg

// TBD: The beginning comments probably need to be in a markdown file rather than buried here
//    in the source. AK100814

// TBD: Should use 'UndefOr[]' for optional parameters

/*
* SnapSvg.scala
*
* The basic Scala.js - JavaScript bridging of Snap.svg. Tries to do an as
* full a bridge as can be done, without higher abstraction levels, or new
* classes.
*
* We do add types to where possible in the API, and we do change parameter
* names if they seem misleading in the JavaScript API.
*
* The goal is to be on the "skin" of the official Snap.svg API, so e.g. code
* snippets on the Internet would work in Scala.js with minimal changes.
* 
* For a more Scala-esque API, see 'SnapSvgScalaesque.scala' (TBD)
*
* Versions:
*   We are bridging SnapSvg 0.3.0: http://snapsvg.io/docs/
*
* Note: There's a Typescript bridge for SnapSvg 0.2.0 here: https://github.com/RethinkFlash/snapsvg-typescript/blob/master/snapsvg.d.ts
*     but the bridge is not very thorough, more like a shorthand.
*     - many methods return 'Object' without further description
*     - multiple prototype functions are simply declared as varargs (= not much help)
*     - optional parameters are just marked '?' without showing what combinations make sense, what not
*     - elements are all represented with the same type ('Element') though they are clearly a family of types
*     - functions (callbacks) are just marked 'Function' - no info on their parameters or return values
*     - at least one mistake ('pop' taking arguments)
*
* All in all, the Typescript bridge is not enough to generate a suitable Scala.js binding.
*/

import scala.scalajs.js

//import js.{Dynamic, UndefOr, ThisFunction, ThisFunction0, Object, Number, Any => JAny, Function => JFn}
//import js.annotation.JSBracketAccess

import js.annotation.JSName

// Note: Order of the declarations tries to follow the Snap.svg API documentation
//      (http://snapsvg.io/docs/) - more or less (the doc itself is jumpy). AK030814
//
// Declarations of the Snap.svg TypeScript interface have been used as a comparison
// at places (but it applies to SnapSvg 0.2.0, at the moment). Note: license of the
// typescript SnapSvg bridge is not stated at their site, so we cannot fully depend
// on it or reuse its code, anyhow. https://github.com/RethinkFlash/snapsvg-typescript

/*
* Note: When it comes to object definitions, we cannot have inner traits or objects
*     within 'js.Object' -derived entities (seems like a Scala.js rule):
*   <<
*     Traits, classes and objects extending js.Any may not have inner traits, classes or objects
*   <<
*/

/*
* Note to author of SnapSvg:
*   It's slightly annoying how copy-paste of text does not work in the browser (Safari)
*   on the site: http://snapsvg.io/docs/ Why is that? AK030814
*/

/* Scala note:
*
* Use of 'js.Function0[_]' instead of 'js.Function0[Unit]' for functions whose return value
* is not meant to be consumed allows any functions to be given. Without this, the last expression
* of the function bodies would need to be '()', creating the 'Unit'. (Thanks for Sébastien on 
* Scala.js mailing list for clarifying this - it is a normal Scala feature, though). AK310814
*/

/*
* This is the main 'Snap.*' namespace, mapped in the 'package.scala' file.
*
* tbd. Or should we use '@JSName(Snap)' here?
*
* tbd. This should be 'object' based on what I understand of the http://www.scala-js.org/doc/calling-javascript.html
*     but if it is, 'package.scala' does not find it. AK120814
*/
@js.native
object Snap extends js.Object {
  def apply( width: Double, height: Double ): SnapPaper = js.native
  def apply( DOM: SnapSVGElement ): SnapPaper = js.native

  // tbd. What is the param type? "array of elements" = 'Array[SnapSVGElement]' or 'Array[SnapElement]'?
  // tbd. What is the return type? "will return set of elements"
  //
  def apply( arr: js.Array[SnapSVGElement] ): Set[SnapElement] = js.native
  def apply( query: String ): SnapPaper = js.native

  // We'll have it here to keep compatibility when porting JavaScript SnapSvg code.
  // The 'Any' (map values) would be strings, numbers (do we have a way to describe that?)
  //
  // Scalaesque: Make deprecated, point at using string interpolation or '.format' instead.
  //
  def format( token: String, json: js.Object ): String = js.native

  // Scalaesque: Make deprecated, point at using 'Math.toRadians()' and 'Math.toDegrees()' instead
  //
  def rad( deg: Double ): Double = js.native
  def deg( rad: Double ): Double = js.native

  // Scalaesque: Could have a type for 'Angle' that also takes care of conversion
  //    ('.asDeg', '.asRad'). The return values here are degrees, but it's not explicit anywhere.
  //
  def angle( x1: Double, y1: Double, x2: Double, y2: Double ): Double = js.native
  def angle( x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double ): Double = js.native

  // "handy replacement for 'typeof' operator"
  //
  //  'type': "string" | "function" | "number" | etc. (JavaScript type names)
  //
  // Scalaesque: Probably not needed since we have actual types.
  //
  @deprecated("Probably not needed in SnapSvg.scala", "")
  def is( o: Any, `type`: String ): Boolean = js.native
  
  // Note: We know that the default tolerance is 10 (SnapSvg docs) but it's best 
  //      not to repeat it here - we're simply passing on the calls to JavaScript.
  //
  def snapTo( values: Array[Double], value: Double ): Double = js.native
  def snapTo( values: Array[Double], value: Double, tolerance: Double ): Double = js.native
  def snapTo( step: Double, value: Double ): Double = js.native
  def snapTo( step: Double, value: Double, tolerance: Double ): Double = js.native

  // See available input strings in SnapSvg docs.
  //
  // Scalaesque: We'd have just 'SnapRGB' returned and a malformed string would throw
  //          an exception (the SnapSvg API could have done that in JavaScript as well,
  //          but doesn't).
  //
  def getRGB( color: String ): SnapRGBorError = js.native

  def hsb( h: Double, s: Double, b: Double ): String = js.native
  def hsl( h: Double, s: Double, l: Double ): String = js.native
  def rgb( r: Double, g: Double, b: Double ): String = js.native
  def color( clr: String ): SnapRGB_HSVLorError = js.native
  def hsb2rgb( h: Double, s: Double, v: Double ): SnapRGB = js.native
  def hsl2rgb( h: Double, s: Double, l: Double ): SnapRGB = js.native
  def rgb2hsb( r: Double, g: Double, b: Double ): SnapHSB = js.native
  def rgb2hsl( r: Double, g: Double, b: Double ): SnapHSL = js.native
    
  def parsePathString( path: String ): Array[SnapSegment] = js.native
  def parsePathString( arr: Array[SnapSegment] ): Array[SnapSegment] = js.native

  def parseTransformString( transform: String ): Array[SnapTransformation] = js.native
  def parseTransformString( arr: Array[SnapTransformation] ): Array[SnapTransformation] = js.native

  // TBD SNAPSVG DOCUMENTATION AMBGUITY: 
  //    'selectAll' return value is said to be: "set or array of Element". At the same time, 
  //    the return value graphic is only 'Element' with caption "the current element"
  //    What is the right return type for 'selectAll'?
  //
  def select( query: String ): SnapElement = js.native
  def selectAll( query: String ): Any /*Array[SnapElement] | Set[SnapElement] | SnapElement TBD*/ = js.native

  // Scalaesque: could use a 'Duration' type for the duration (making 'ms' etc. endings compulsory, not just a number)
  //
  // TBD: What are the parameters of the callback function?
  //
  // TBD: What is the return type ? "animation object" what fields?
  //
  def animation( attr: js.Object, duration: Int /*ms*/, easing: js.Function1[Double,Double], callback: js.Function /*tbd: precisely?*/ ): SnapAnimMina = js.native
  
  def animate( from: Double, to: Double, setter: js.Function1[Double,_], duration: Int /*ms*/, easing: js.Function1[Double,Double], callback: js.Function /*tbd: precisely?*/ ): SnapAnimMina = js.native
  def animate( from: Double, to: Double, setter: js.Function1[Double,_], duration: Int /*ms*/, easing: js.Function1[Double,Double] ): SnapAnimMina = js.native
  def animate( from: Double, to: Double, setter: js.Function1[Double,_], duration: Int /*ms*/ ): SnapAnimMina = js.native

  // SNAPSVG API: When would 'from' and 'to' be arrays? Doc says so but what is the use case?
  //
  //def animate( from: js.Array[Double], to: js.Array[Double], setter: js.Function1[Double,_], duration: Int /*ms*/, easing: js.Function1[Double,Double], callback: js.Function /* tbd precisely?*/ ): SnapAnimMina = js.native
  //def animate( from: js.Array[Double], to: js.Array[Double], setter: js.Function1[Double,_], duration: Int /*ms*/, easing: js.Function1[Double,Double] ): SnapAnimMina = js.native
  //def animate( from: js.Array[Double], to: js.Array[Double], setter: js.Function1[Double,_], duration: Int /*ms*/ ): SnapAnimMina = js.native

  // SNAPSVG API question: how is this different from 'Snap.fragment' with one argument? Is it the same?
  //                      - what is the relation of 'Element' and 'Fragment'?
  //
  def parse( svg: String ): SnapFragment = js.native
  
  // Note: Without the first param, these both would end up the same after type erasure.
  //
  def fragment( arg0: String, args: String* ): SnapFragment = js.native
  def fragment( arg0: SnapElement, args: SnapElement* ): SnapFragment = js.native

  // These return the 'XMLHttpRequest' object, "just in case"
  //
  // Scalaesque: do we have a type for the 'XMLHttpRequest' object?
  //        - we probably don't need the 'scope' parameter
  //
  // tbd: callback parameters?
  //
  def ajax( url: String, postData: js.Object, callback: => Unit, scope: js.Object ): js.Object = js.native
  def ajax( url: String, postData: String, callback: => Unit, scope: js.Object ): js.Object = js.native
  def ajax( url: String, callback: => Unit, scope: js.Object ): js.Object = js.native

  // SNAPSVG API documentation bug: should return 'Fragment' (says "loads .. as Fragment") but no return value
  //  
  // tbd: callback parameters?
  //
  def load( url: String, callback: js.Function1[SnapFragment, Unit], scope: js.UndefOr[js.Object] = js.undefined): Unit = js.native

  def getElementByPoint( x: Double, y: Double ): SnapElement = js.native

  // SNAPSVG API correction: text says "with four arguments" but there are five.
  //
  // The plugin function could of course also skip any of the tail arguments, in JavaScript
  // (i.e. be 1..5 parameters).
  //
  // Scalaesque: maybe we don't need plugins there?
  //          maybe we can create a more scalaesque API (and what disallows us from simply
  //          stabbing in 'SnapStatic', 'SnapElement' et. al. without having such 'plugin' API?
  //
  def plugin( f: (/*Snap*/ js.Object, /*Element*/ js.Object, /*Paper*/ js.Object, /*global*/ js.Object, /*Fragment*/ js.Object) => Unit ): Unit = js.native

  // 'Snap.Matrix' intentionally not bridged
  
  def matrix( a: Double, b: Double, c: Double, d: Double, e: Double, f: Double ): SnapMatrix = js.native
  def matrix( other: SnapMatrix ): SnapMatrix = js.native

  // SNAPSVG API AMBIGUITY: Should it be 'Snap.mina' in the docs, now just 'mina' (where does it belong to?)
  //
  def mina( a: Double, A: Double, b: Double, B: Double, get: => Double, set: (Double) => Unit, easing: js.Function1[Double,Double] ): SnapAnimDescriptor = js.native

  //val filter= SnapStaticFilter
  //val path= SnapStaticPath
}

/*
* TBD: What is this, actually? Is it just a string, called a 'Segment' in SnapSvg API, or a genuine type?
*/
@js.native
trait SnapSegment extends js.Object {
}


/*--- Colors ---
*
* Note: Conceptually 'SnapRG_HSVLorError' derives of 'SnapRGBorError' (it extends fields to the latter)
*     but we cannot show this as a derives-from relationship as long as the types are derived from
*     'js.Object'.
*
* Scalaesque: Make these types as case classes (without the error field), and use derivation.
*/
@js.native
trait SnapRGB extends js.Object   // { r: Double, g: Double, b: Double }
@js.native
trait SnapHSB extends js.Object   // { h: Double, s: Double, b: Double }
@js.native
trait SnapHSL extends js.Object   // { h: Double, s: Double, l: Double }

@js.native
trait SnapRGBorError extends js.Object  // { r: Double, g: Double, b: Double, hex: String, error: Boolean }
@js.native
trait SnapHSBorError extends js.Object  // { h: Double, s: Double, b: Double, error: Boolean }
@js.native
trait SnapHSLorError extends js.Object  // { h: Double, s: Double, l: Double, error: Boolean }
@js.native
trait SnapRGB_HSVLorError extends js.Object  // { ..fields of 'SnapRGBorError'..,  h: Double, s: Double, v: Double, l: Double }


/*--- DOM Elements ---
*
* SnapSvg provides its own DOM traversing functions. Some of these are generic
* while others are more SVG specific. Let's bridge it all...
*/
@js.native
trait SnapElement extends js.Object {

  //private val alert = js.Dynamic.global.alert   // debugging help

  // Returns a DOM node reference (TBD: what's the right type for that?)
  //
  def node( x: Double, y: Double, width: Double, height: Double, refX: Double, refY: Double ): js.Object = js.native

  // SNAPSVG DOCUMENTATION AMBIGUITY
  //
  // TBD: So are we setting/getting or both the element tag name (presumably 'path','rect','ellipse' etc.)
  //  
  def `type`( tstr: String ): String = js.native    // "SVG tag name of the element"
  
  // Scalaesque: express a more narrow type than 'Any' for the strings, numbers or booleans attribute values can actually be.
  //
  // Scalaesque: If the set of attributes is finite (it is, right?) we can create the appropriately
  //          typed setters and getters for them all, type specifically (a lot of work, yes). This
  //          'attr' way could be preserved as a syntax middle stop ('el.attr.color = ') or simply
  //          bypassed ('el.color = '). 'Attr' is still useful for setting multiple attributes at once.
  //
  def attr( map: js.Object ): this.type = js.native   // returns "the current element"
  def attr( key: String ): js.Any = js.native               // get an attribute (would that always be 'String' or can it be 'Number' / 'Boolean'?

  def getBBox: SnapBBox = js.native
  
  // SNAPSVG DOCUMENTATION AMBIGUITY
  //
  // The way it's probably meant is that given a parameter works as setter, and returns
  // the "current element" whereas giving no parameter returns the "transformation descriptor".
  // However, the current (0.3.0) doc does not portray it this way - in fact it shows 'tstr'
  // as a compulsory parameters.
  //   
  def transform( tstr: String ): this.type = js.native
  def transform(): SnapTransformation = js.native
  
  def parent: SnapElement = js.native

  // SNAPSVG DOCUMENTATION AMBIGUITY
  //
  // SnapSvg API doc shows 'el' can be 'Element' or 'Set' - is that ordered or not?
  // Only appending an ordered set (aka list) makes sense. The SnapSvg API doc should be 
  // more precise on these.
  //
  def append( el: SnapElement ): this.type = js.native
  def append( el: Seq[SnapElement] ): this.type = js.native

  // We can probably leave such aliases away (just have one word / field name for one thing)
  //
  //def add(v: Any*) = append(v)      // wasn't able to use 'val add = append _' because of multiple prototypes
    
  // SNAPSVG DOCUMENTATION AMBIGUITY
  //
  // Unlike in 'append', in 'prepend' the parameter is supposed to be only a single element,
  // not a 'set'.
  //
  def prepend( el: SnapElement ): this.type = js.native       // places 'el' within the current element, as 1st

  // Note: 'appendTo' moved here (introduced a bit higher up in SnapSvg docs) to keep the
  //      similar methods together.
  //
  def appendTo( parent: SnapElement ): this.type = js.native      // places this element within 'parent' as last
  def prependTo( parent: SnapElement ): this.type = js.native      // places this element within 'parent' as 1st

  // SNAPSVG DOCUMENTATION AMBIGUITY
  //
  // For 'before' and 'after' the returned value is said to be "the parent element".
  // Does it really mean that - the common parent element of the current element, and 'el',
  // or is it a glitch? Here we're thinking it really is the common parent (so we're not
  // making it 'this.type'). TBD: how is implementation?
  //
  // Same for 'insertBefore' and 'insertAfter' as well
  //
  def before( el: SnapElement ): SnapElement /*or 'this.type'?*/ = js.native
  def after( el: SnapElement ): SnapElement /*or 'this.type'?*/ = js.native

  def insertBefore( el: SnapElement ): SnapElement /*or 'this.type'?*/ = js.native
  def insertAfter( el: SnapElement ): SnapElement /*or 'this.type'?*/ = js.native

  def remove: this.type = js.native

  // Scalaesque: to which SVG element types does 'select' (searching through nested elements)
  //          actually refer? Anything other than <group>? We could limit it to only those.
  //
  // SNAPSVG DOCUMENTATION AMBIGUITY
  //
  // Did we get it right? 'select' chooses one, 'selectAll' chooses all (the
  // API doc wording isn't explicitly saying this).
  //
  def select( query: String ): SnapElement = js.native
  def selectAll( query: String ): Seq[SnapElement] = js.native

  // SNAPSVG DOCUMENTATION AMBIGUITY
  //
  // Also this probably works as a getter as well as a setter? The doc shows the getter
  // character but why would there then be a 'value' argument?
  //
  // Also: "Returns: result of query selection" must be wrong?
  //
  def asPX( attr: String ): Double = js.native
  def asPX( attr: String, value: String ): Double = js.native   // TBD: does it return the value as pixels, or the element?
  
  def use: SnapUseElement = js.native

  override def clone: this.type = js.native
  
  def toDefs: this.type = js.native

  def toPattern( x: Double, y: Double, width: Double, height: Double ): SnapPatternElement = js.native   
  
  // Scalaesque: could call this 'toMarker' since it's similar to 'toPattern' (and 'pattern' is deprecated)
  //
  def marker( x: Double, y: Double, width: Double, height: Double, refX: Double, refY: Double ): SnapMarkerElement = js.native
  
  // SNAPSVG API AMBIGUITY: "Returns a set of animations that..." does that mean a list, or really a set?
  //
  def inAnim: Array[SnapAnimStatus] = js.native

  def stop: this.type = js.native

  // SnapSvg API inconsistency: 'attrs' when normally for arrays, it would be in singular ('attr')
  //
  // Scalaesque: attr map would have certain keys, with certain types for values (String, Int, Double, Boolean)
  //        use of Duration type
  //        could use composition ('.easing') or named parameters for 'easing' and 'callback'
  //
  def animate( attr: js.Object, duration: Int /*ms*/ ): this.type = js.native
  def animate( attr: js.Object, duration: Int /*ms*/, easing: js.Function1[Double,Double] ): this.type = js.native
  def animate( attr: js.Object, duration: Int /*ms*/, easing: js.Function1[Double,Double], callback: js.Function0[_] ): this.type = js.native
    // tbd: what is the parameter for 'callback'?

  // Scalaesque: do we even need these? Doesn't Scala provide enough data connection mechanisms?
  //            (but so would JavaScript - what's the use case for these?)
  //
  def data( key: String, value: Any ): this.type = js.native  
  def data( key: String ): Any = js.native
    //
  def removeData( key: String ): this.type = js.native
  def removeData(): this.type = js.native

  def outerSVG: String = js.native
  def innerSVG: String = js.native
  override def toString: String = js.native    // alias for 'outerSVG'

  // SNAPSVG DOCUMENTATION AMBIGUITY:
  //    The doc says 'flag' and "determine whether the class should be added or removed".
  //    This is not enough to tell whether 'true' adds or removes. The param could instead
  //    be called 'add'. It's not really a 'toggle' either if it works this way - a toggle
  //    would swap the current status of the classes.
  //
  // Scalaesque: is 'toggleClass' meaningful? Maybe we leave it out (can be crafted using
  //            the other three features anyways).
  //
  def addClass( value: String ): this.type = js.native
  def removeClass( value: String ): this.type = js.native
  def hasClass( value: String ): Boolean = js.native
  def toggleClass( value: String, flag: Boolean ): this.type = js.native

  // TBD: params for the handlers weren't mentioned in the documentation, must look at source,
  //    samples or try out.
  //
  def click( handler: js.Function0[_] ): this.type = js.native

  def unclick( handler: => Unit ): this.type = js.native
  def dblclick( handler: => Unit ): this.type = js.native
  def undblclick( handler: => Unit ): this.type = js.native
  def mousedown( handler: => Unit ): this.type = js.native
  def unmousedown( handler: => Unit ): this.type = js.native
  def mousemove( handler: => Unit ): this.type = js.native
  def unmousemove( handler: => Unit ): this.type = js.native
  def mouseout( handler: => Unit ): this.type = js.native
  def unmouseout( handler: => Unit ): this.type = js.native
  def mouseover( handler: => Unit ): this.type = js.native
  def unmouseover( handler: => Unit ): this.type = js.native
  def mouseup( handler: => Unit ): this.type = js.native
  def unmouseup( handler: => Unit ): this.type = js.native
  def touchstart( handler: => Unit ): this.type = js.native
  def untouchstart( handler: => Unit ): this.type = js.native
  def touchmove( handler: => Unit ): this.type = js.native
  def untouchmove( handler: => Unit ): this.type = js.native
  def touchend( handler: => Unit ): this.type = js.native
  def untouchend( handler: => Unit ): this.type = js.native
  def touchcancel( handler: => Unit ): this.type = js.native
  def untouchcancel( handler: => Unit ): this.type = js.native
  
  // Scalaesque: do we need the context parameters?
  //
  def hover( f_in: js.Function0[_], f_out: js.Function0[_], icontext: js.Object = null, ocontext: js.Object = null ): this.type = js.native
  def unhover( f_in: js.Function0[_], f_out: js.Function0[_] ): this.type = js.native

  // SNAPSVG API consistency: 'undrag' is not defined like other 'un...' functions.
  //          It does not take parameters, but removes all drag associations.
  //
  def drag( 
    onmove: (/*dx:*/ Double, /*dy:*/ Double, /*x:*/ Double, /*y:*/ Double, /*event:*/ js.Object) => Unit, 
    onstart: (/*x:*/ Double, /*y:*/ Double, /*event:*/ js.Object) => Unit, 
    onend: (/*event:*/ js.Object) => Unit, 
    mcontext: js.Object = null, 
    scontext: js.Object = null, 
    econtext: js.Object = null ): this.type = js.native
  def undrag: Unit = js.native    // TBD: does it not return anything, or does it return the element ('this.type')?
}

// Scalaesque: more care on which methods are truly for all elements, which are for a certain kind / kinds only.
//
@js.native
trait SnapRectElement extends SnapElement {}
@js.native
trait SnapCircleElement extends SnapElement {}
@js.native
trait SnapEllipseElement extends SnapElement {}
@js.native
trait SnapImageElement extends SnapElement {}

// Scalaesque: could add to the 'getSubpath' API ability to leave out 'from' or 'to', or use
//          negative ones (measuring from the end)
//
@js.native
trait SnapPathElement extends SnapElement {
  def getTotalLength: Double = js.native
  def getPointAtLength( lenght: Double ): PointAndAngle = js.native
  def getSubpath( from: Double, to: Double ): String = js.native
}

@js.native
trait SnapGroupElement extends SnapElement {}
@js.native
trait SnapSVGElement extends SnapElement {}
@js.native
trait SnapMaskElement extends SnapElement {}
@js.native
trait SnapPatternElement extends SnapElement {}
@js.native
trait SnapUseElement extends SnapElement {}
@js.native
trait SnapTextElement extends SnapElement {}
@js.native
trait SnapLineElement extends SnapElement {}
@js.native
trait SnapPolylineElement extends SnapElement {}
@js.native
trait SnapGradientElement extends SnapElement {}
@js.native
trait SnapFilterElement extends SnapElement {}
@js.native
trait SnapMarkerElement extends SnapElement {}


/*--- Bounding box ---
*
* Scalaesque: looks like a case class to me.
*     - with accessor methods for 'height' and 'width' (or 'h' and 'w'), or
*       maybe we'll just implement either 'h' or 'height' throughout is Scalaesque.
*/
@js.native
trait SnapBBox extends js.Object
/* {
    val cx: Double
    val cy: Double
    val h: Double
    val height: Double
    val path: String
    val r0, r1, r2: Double
    val vb: String
    val w: Double
    val width: Double
    val x,x2,y,y2: Double
  }
*/


/*
* Scalaesque: case class
*/
@js.native
trait SnapTransformation extends js.Object
/*
{
    val string: String   // transformation string (SnapSvg should call it 'tstring' to be in-par with rest of the API)
    val globalMatrix: SnapMatrix
    val localMatrix: SnapMatrix
    val diffMatrix: SnapMatrix
    val global: String
    val local: String
    val toString: => String  // "returns 'string' property"
}
*/


/*
* Scalaesque: case class
*/
@js.native
trait SnapAnimStatus extends js.Object
/*
{
  val anim: js.Object         // "animation object"
  val mina: SnapAnimMina      // "mina object"
  val curStatus: Double       // 0.0 .. 1.0
  val status: js.Function     // "gets or sets the status of the animation"
  val stop: => Unit           // "stops the animation"
}
*/


/*
* Scalaesque: case class
*/
@js.native
trait SnapAnimMina extends js.Object
/*
{
  val id: String
  val duration: js.Function1[Int,Int]   // "gets or sets the duration of the animation" (dynamic function, gets either 0 or 1 params)
  val easing: js.Function1[Double,Double]
  val speed: js.Function        // "gets or sets the speed of the animation"
  val status: js.Function       // "gets or sets the status of the animation"
  val stop: => Unit             // "stops the animation"
}
*/

/*
* Scalaesque: case class
*     - instead of 'dur' and 'spd' we could have more describing names (but 'duration'
*       and 'speed' are taken by the functions. There's little shorthand otherwise in the API.
*
* SNAPSVG API question:
*     - what's the difference between 's' and 'status', 'dur' and 'duration', 'spd' and 'speed'?
*       Are the first ones snapshots of the getter values, at the time the structure was created?
*/
@js.native
trait SnapAnimDescriptor extends js.Object
/*
{
    val id: String
    val start: Double
    val end: Double 
    val b: Double
    val s: Double
    val dur: Double
    val spd: Double
    val get: => Double
    val set: Double => Unit
    val easing: js.Function1[Double,Double]      (Scalaesque: make a separate 'EasingFunc' alias)
    val status: js.Function         // "getter/setter" so params vary, Scalaesque: do as getter/setter of a value
    val speed: js.Function          // -''-
    val duration: js.Function       // -''-
    val stop: => Unit
    val pause: => Unit
    val resume: => Unit
    val update: => Unit
}
*/

/*
* SNAPSVG API question: What is the relation of 'Fragment' with 'Element'?
*     Fragment only has these two methods, and for those the API points to
*     similar of 'Element'.
*/
@js.native
trait SnapFragment extends SnapElement {
  def select: Unit = js.native
  def selectAll: Unit = js.native
}

/*
* Scalaesque: work more only with matrices (not the numbers as such)
*             - maybe add operators for + - etc. (in addition to the methods)
*/
@js.native
trait SnapMatrix extends js.Object {
  def add( a: Double, b: Double, c: Double, d: Double, e: Double, f: Double ): SnapMatrix = js.native
  def add( o: SnapMatrix ): SnapMatrix = js.native
  def invert: SnapMatrix = js.native

  /* Note: cannot use 'clone' as a method without overriding (do we want to override, is it coming from 'js.Object'?)
  */
  override def clone: SnapMatrix = js.native

  // SNAPSVG API AMBIGUITY: does not mention return types - do these edit the matrix
  //          in place, or return a new one?
  //
  // Note: using 'scalex', 'scaley' instead of 'x' and 'y' - and 'dx','dy' for transitions:
  //      more clear and consistent with 'Matrix.split()' output.
  //
  // Scalaesque: these should return a new matrix.
  //        - angle should be given as unit-agnostic (or 'Degrees' type)
  //
  def translate( dx: Double, dy: Double ): SnapMatrix = js.native
    //
  def scale( scalexy: Double ): SnapMatrix = js.native
  def scale( scalex: Double, scaley: Double ): SnapMatrix = js.native
  def scale( scalex: Double, scaley: Double, cx: Double, cy: Double ): SnapMatrix = js.native
    //
  def rotate( deg: Double, cx: Double, cy: Double ): SnapMatrix = js.native

  // Scalaeque: make a 'Point' class and use it for these, in addition to separate 'x', 'y' coords.
  //        - make 'xy' method (or simply 'apply') that runs the transformation on both coordinates.
  //
  def x( x: Double, y: Double ): Double = js.native
  def y( x: Double, y: Double ): Double = js.native

  def determinant: Double = js.native
  
  def split: SnapSplit = js.native

  def toTransformString: String = js.native
}

/*
* Scalaesque: case class
*     - 'rotate' probably with a 'Deg' (or unit agnostic angle class)
*
* Note: should use 'dx','dy' everywhere when talking about translations
*/
@js.native
trait SnapSplit extends js.Object
/*
{
  val dx: Double
  val dy: Double
  val scalex: Double
  val scaley: Double
  val shear: Double
  val rotate: Double (deg)
  val isSimple: Boolean
*/


/*
* Paper
*/
@js.native
trait SnapPaper extends SnapElement {

  // SNAPSVG API inconsistency: doc says "and no attributes" but there's an attribute parameter.
  //
  def el( name: String, attr: js.Object ): SnapElement = js.native
  
  // Better to let actual SnapSvg handle default values.
  //
  def rect( x: Double, y: Double, width: Double, height: Double ): SnapRectElement = js.native
  def rect( x: Double, y: Double, width: Double, height: Double, r: Double ): SnapRectElement = js.native
  def rect( x: Double, y: Double, width: Double, height: Double, rx: Double, ry: Double ): SnapRectElement = js.native

  // SNAPSVG API consistency: since the coords are center coords, they could be called 'cx','cy' (not 'x','y')
  //
  def circle( cx: Double, cy: Double, r: Double ): SnapCircleElement = js.native

  // SNAPSVG API question: doc says "x (y) offset position". It just means position of the upper left corner, right?
  //
  // Scalaesque: could make a 'URI' class since those are used in many places (s.a. 'src').
  //
  // SNAPSVG API AMBIGUITY: there are two return types in doc: "the image element" and
  //          "Snap element object of type image". Same thing, right?
  //
  def image( src: String, x: Double, y: Double, width: Double, height: Double ): SnapImageElement = js.native

  // SNAPSVG API consistency: also here, rather 'cx','cy' than 'x','y'
  //
  def ellipse( cx: Double, cy: Double, rx: Double, ry: Double ): SnapEllipseElement = js.native

  // SNAPSVG API question: The prototype shows that 'path' param is optional. Does the function
  //        return the current path if it's omitted? Could be stated in the doc.
  //
  // SNAPSVG API question: no info on what 'path' would return. What does it return?
  //
  def path( path: String ): SnapPathElement = js.native   // TBD: returns something?
  def path: SnapPathElement = js.native     // TBD: returns 'SnapPathElement' or 'String'?

  // Scalaesque: just one, 'g' or 'group'
  //
  def g( els: SnapElement* ): SnapGroupElement = js.native
  //val group= g _  // alias

  // SNAPSVG API CONSISTENCY: All the parameters are marked 'optional' in the text
  //        but not in square brackets in the prototype. This is not akin to how
  //        other methods are presented. Also, there needs to be some logic between
  //        what are optional: viewbox dimensions need to be either in, or all out (right?).
  //
  // Scalaesque: there's really two boxes here. Maybe 'viewbox' should be presented as such
  //        (with a 'Rect' case class that takes 'x','y','w','h').
  //
  def svg( x: Double, y: Double, width: Double, height: Double, vbx: Double, vby: Double, vbw: Double, vbh: Double ): SnapSVGElement = js.native

  // SNAPSVG API ISSUE: 'mask' is said to be "equivalent in behaviour to Paper.g" but it
  //            has no parameters.
  //
  def mask( els: SnapElement* ): SnapMaskElement = js.native

  // SNAPSVG API CONSISTENCY: Same as 'svg' issue, with the marking of optional params.
  //
  // SNAPSVG API BUG: Also, it's not "equivalent in behaviour to Paper.g" - it's having the
  //          'svg' method's parameter list. Some copy-paste error here, for sure..
  //
  def ptrn( els: SnapElement* ): SnapPatternElement = js.native

  //def ptrn( x: Double, y: Double, width: Double, height: Double, vbx: Double, vby: Double, vbw: Double, vbh: Double ): SnapPatternElement = js.native     // as in doc

  // Each 'id' can be a String or a SnapElement, individually
  //
  // Scalaesque: allow either multiple id's or elements but not both, mixed.
  //        (if needed, we can make a 'IdOrElement' class and converters to it)
  //
  def use( ids: js.Object* ): SnapUseElement = js.native

  // tbd. does having 'text' both as param name and method name cause problems?
  //
  def text( x: Double, y: Double, text: String ): SnapTextElement = js.native
  def text( x: Double, y: Double, text: js.Array[String] ): SnapTextElement = js.native

  def line( x1: Double, y1: Double, x2: Double, y2: Double ): SnapLineElement = js.native
  
  // Snap.svg documentation says "points" but it really means "sequtive pairs of numbers", 
  // s.a. in the sample: '[10, 10, 100, 100]' and '(10, 10, 100, 100)'.
  //
  // Scalaesque: make it take actual list of Points.
  //        - if 'polyline' and 'polygon' are the same, bring in only 'polyline'
  //  
  def polyline( points: Double* ): SnapPolylineElement = js.native
  //val polygon = polyline _    // just an alias?

  def gradient( s: String ): SnapGradientElement = js.native

  // Scalaesque: 
  //  - we might not wish to override 'clone' and 'toString' - rather use 'outerSVG'
  //    as with the Elements
  //
  override def toString: String = js.native    // SVG code for the Paper

  def clear(): Unit = js.native       // has side-effects, thus '()' retained

  def filter( filstr: String ): SnapFilterElement = js.native
}

/*
* mina
*
* Scalaesque: an object, no inheritance from 'js.Object' (hmm, should we do so even here?) tbd
*
* tbd. having 'object' makes this compile (for Croc1 demo), but it's really a namespace. Which one is "right" in Scala.js point of view? AK310814
*/
@js.native
object mina extends js.Object {
  def time: Double = js.native    // tbd. maybe it returns 'Long'?
  def getById(id: String): SnapAnimDescriptor = js.native

  def linear(n: Double): Double = js.native
  def easeout(n: Double): Double = js.native
  def easein(n: Double): Double = js.native
  def easeinout(n: Double): Double = js.native
  def backin(n: Double): Double = js.native
  def backout(n: Double): Double = js.native
  def elastic(n: Double): Double = js.native
  def bounce(n: Double): Double = js.native
}


/*
* Snap.filter.* (cannot declare an object within 'js.Object' derived traits)
*
* SNAPSVG API AMBIGUITY: If 'blur' is called with one parameter, does it blur 'x'
*         and 'y' the same, or only blur the 'x'? Current doc implies the latter
*         but radius handling (e.g. for rectangle rounded corners) works like first.
*/
object SnapStaticFilter {
  def blur( x: Double ): String = ???
  def blur( x: Double, y: Double ): String = ???

  // SNAPSVG API concern: Having optional parameters in the middle of the parameter list is... confusing. 
  //
  // Scalaesque: don't reproduce the "missing params in the middle". Use default values (even 'null')
  //          and named parameters instead.
  //
  def shadow( dx: Double, dy: Double, blur: Double, color: String, opacity: Double ): String = ???
  def shadow( dx: Double, dy: Double, color: String, opacity: Double ): String = ???
  def shadow( dx: Double, dy: Double, opacity: Double ): String = ???

  // Scalaesque: do the angle with 'Degree' or unit agnostic class
  //
  def grayscale( amount: Double ): String = ???
  def sepia( amount: Double ): String = ???
  def saturate( amount: Double ): String = ???
  def hueRotate( angle: Double ): String = ???
  def invert( amount: Double ): String = ???
  def brightness( amount: Double ): String = ???
  def contrast( amount: Double ): String = ???
}


/*
* Snap.path
*
* Scalaesque: could have 'Snap.path.getTotalLength', 'Snap.path.getPointAtLength' 
*           and '.getPointAtLength' be methods of the 'SnapPathElement' instead.
*/
object SnapStaticPath {
  // SNAPSVG API inconsistency: why is the returned angle field called '.alpha'?
  //            It should also be stated it's in degrees.
  //
  def getTotalLength( path: String ): Double = ???
  def getPointAtLength( path: String, length: Double ): PointAndAngle = ???

  // SNAPSVG API suggestions: could allow either or both 'from' and 'to' be absent (meaning start of path / end of path)
  //      could allow 'from' and 'to' be negative, meaning measured from the end of the path.
  //
  def getSubPath( path: String, from: Double, to: Double ): String = ???
  
  // Scalaesque: use Point class
  //
  def findDotsAtSegment( p1x: Double, p1y: Double, c1x: Double, c1y: Double, c2x: Double, c2y: Double, p2x: Double, p2y: Double, t: Double ): PointAnchorsAndAlpha = ???
  
  // Scalaesque: use Points
  //
  // SNAPSVG API question: How come the array variant has an "array of six points" when the non-array
  //          one takes four points (eight coordinates). Are they different?
  //
  def bezierBBox( p1x: Double, p1y: Double, c1x: Double, c1y: Double, c2x: Double, c2y: Double, p2x: Double, p2y: Double ): BBoxWidthHeight = ???
  def bezierBBox( p1x: List[Double] /*6 (or 8?) numbers*/ ): BBoxWidthHeight = ???

  // SNAPSVG API bug?: 'x' and 'y' are marked as 'string' in the documentation
  //          - what would the 'bbox' actual type be? probably bounding box object (not 'string')
  //
  // Scalaesque: could have this as methods of the 'BBox' class
  //
  def isPointInsideBBox( bbox: BBox /*string in docs*/, x: Double, y: Double ): Boolean = ???

  // SNAPSVG API question: Are the Bbox parameters' types really 'string' (and not BBox objects)?
  //
  // Scalaesque: rather a method of 'BBox' class (with another bounding box)
  //
  def isBBoxIntersect( bbox1: BBox, bbox2: BBox ): Boolean = ???

  // Scalaesque: rather method of 'Path' (make it just that - not 'SnapPathElement' itself, just the
  //          abstract path is enough)
  //
  def intersection( path1: String, path2: String ): Seq[IntersectInfo] = ???

  // Scalaesque: rather a method of Path
  //  
  def isPointInside( path: String, x: Double, y: Double ): Boolean = ???
  def getBBox( path: String ): BBoxWidthHeight = ???

  // SNAPSVG API AMBIGUITY: what are these returning? Doc tag is 'array' but says 'path string'
  //  
  // Scalaesque: rather methods of Path
  //
  def toRelative( path: String ): String = ???     // return type unsure TBD
  def toAbsolute( path: String ): String = ???     // return type unsure TBD

  def toCubic( path: String ): Seq[SnapSegment] = ???
  def toCubic( path: Seq[SnapSegment] ): Seq[SnapSegment] = ???

  // Scalaesque: rather method of Path
  //  
  def map( path: String, matrix: SnapMatrix ): String = ???
}

// Scalaesque: Make also '.xy' for a point, and have '.angle' instead of '.alpha'
//            - make '.angle' use 'Degree' or unit-agnostic angle class
//
@js.native
trait PointAndAngle extends js.Object
/*
{
  val x: Double
  val y: Double
  val alpha: Double   // angle in degrees (though unit not mentioned)
}
*/ 

// Scalaesque: Make  also '.xy' for a point (in all the levels)
//        Replace 'm', 'n', 'start', 'end' by points (actually keeps the current API) 
//        Maybe 'alpha' is appropriate here instead of 'angle' - need to check that from somewhere.
//
@js.native
trait PointAnchorsAndAlpha extends js.Object
/*
{
  val x: Double
  val y: Double
  val m: { val x: Double; val y: Double }
  val n: { val x: Double; val y: Double }
  val start: { val x: Double; val y: Double }
  val end: { val x: Double; val y: Double }
  val alpha: Double   // deg ?
}
*/

// Scalaesque: use Points
//
// Note: It's not clear whether some of the return values are these, or all 'BBoxWidthHeAight'
//
@js.native
trait BBox extends js.Object
/*
{
  val x: Double
  val y: Double
  val x2: Double
  val y2: Double
}

*/
// Scalaesque: use Points
//        - make 'width' and 'height' as functions (not members)
//
@js.native
trait BBoxWidthHeight extends BBox
/*
{
  val x: Double
  val y: Double
  val x2: Double
  val y2: Double
  val width: Double
  val height: Double
}
*/

// Scalaesque: use Point (for x,y), BezPoints for 'bez1' and 'bez2' (with p1,p2,c1,c2)
//
@js.native
trait IntersectInfo extends js.Object
/*
{
  val x: Double
  val y: Double
  val t1: Double
  val t2: Double
  val segment1: Double
  val segment2: Double
  val bez1: List[Double]
  val bez2: List[Double]
}
*/

/*
* Scalaesque: let's do this deriving of normal Scala collections, then adding
*           the animate etc. onto it.
*/
@js.native
trait SnapSet extends js.Object {
  // SNAPSVG API AMBIGUITY: says "each argument" but none are listed in the prototype of 'push'.
  //          - push "returns original element" - what would that be? would make sense that it returns the set
  //
  def push( args: SnapElement* ): SnapSet = js.native   // TBD: return value unsure
  def pop: SnapElement = js.native

  // Note: "If the callback returns 'false' the loop stops running" (but that does not mean
  //      it needs to return something.
  //
  def forEach( callback: SnapElement => Any, context: js.Object ): this.type = js.native
  
  // SNAPSVG API inconsistency: normally 'attr' is used for plural as well, here 'attrs'
  //
  // Scalaesque: 'attr' can work on the known set (and types) of attributes
  //            - use of Duration class
  //
  def animate( attr: js.Object, duration: Int, easing_f: js.Function1[Double,Double], callback: js.Function0[_] ): SnapElement = js.native

  // This version would animate the set's contents separately - 1st param for 1st entry, and so forth..
  // Is this really neeeded and useful in reality. Multiple sets would do the same.
  //
  // SNAPSVG API comment: 
  //      It's hard to describe this in types, since the params are arrays (2..4 entries long) of different
  //      types (map, double, functions), not JavaScript hashmaps as usually in the SnapSvg API.
  //
  def animate( animation: Seq[js.Object]* ): SnapElement = js.native

  def bind( attr: String, callback: => Unit ): this.type = js.native
  def bind( attr: String, el: SnapElement ): this.type = js.native
  def bind( attr: String, el: SnapElement, eattr: String ): this.type = js.native
  
  def clear(): Unit = js.native

  def splice( index: Int, count: Int, insertion: SnapElement* ): SnapSet = js.native
  def exclude( el: SnapElement ): Boolean = js.native
}
