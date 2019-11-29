package sk.mmx.fifteen

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.MotionEventCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private var mVelocityTracker: VelocityTracker? = null
    private var image: ImageView? = null


    private var direction: String = ""
    private var posX = 0F
    private var posY = 0F

    var gameMatrixArray = Array (4,{IntArray(4)})

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action: Int = MotionEventCompat.getActionMasked(event)

        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                // Reset the velocity tracker back to its initial state.
                mVelocityTracker?.clear()
                // If necessary retrieve a new VelocityTracker object to watch the
                // velocity of a motion.
                mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                // Add a user's movement to the tracker.
                mVelocityTracker?.addMovement(event)
                true
            }
            MotionEvent.ACTION_MOVE -> {

                mVelocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    computeCurrentVelocity(1000)
                    // Log velocity of pixels per second
                    // Best practice to use VelocityTrackerCompat where possible.
                    //

                    // println(("X velocity: ${getXVelocity(pointerId)}"))
                    // println(("Y velocity: ${getYVelocity(pointerId)}"))
                    if (xVelocity.absoluteValue > yVelocity.absoluteValue) {
                        if (xVelocity > 0) {
                            direction = "right"
                        } else {
                            direction = "left"
                        }
                    }
                    if (xVelocity.absoluteValue < yVelocity.absoluteValue) {
                        if (yVelocity > 0) {
                            direction = "down"
                        } else {
                            direction = "up"
                        }
                    }
                }
                true
            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker?.recycle()
                mVelocityTracker = null

                moveBlock(direction)

                true
            }
            MotionEvent.ACTION_CANCEL -> {
                println("CANCEL")
                true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                println("OUTSIDE")
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //create block vars and put them into array
        var block01 = ImageView(this)
        var block02 = ImageView(this)
        var block03 = ImageView(this)
        var block04 = ImageView(this)
        var block05 = ImageView(this)
        var block06 = ImageView(this)
        var block07 = ImageView(this)
        var block08 = ImageView(this)
        var block09 = ImageView(this)
        var block10 = ImageView(this)
        var block11 = ImageView(this)
        var block12 = ImageView(this)
        var block13 = ImageView(this)
        var block14 = ImageView(this)
        var block15 = ImageView(this)
        var blocks = arrayOf(
            block01,
            block02,
            block03,
            block04,
            block05,
            block06,
            block07,
            block08,
            block09,
            block10,
            block11,
            block12,
            block13,
            block14,
            block15
        )



        var index = 100
        var column = 1
        //init image views in block array
        for (item in blocks) {
            item.setImageResource(R.drawable.block)
            item.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            item.layoutParams.width = 100
            item.layoutParams.height = 100

            item.x = (index).toFloat()
            item.y = (column*index).toFloat()

            myLayout.addView(item)

            index+=100
            column+=1

            if (column==5) {
                column=1
                index+=100
            }

        }


        //create and fill game matrix
        var x = 0

        for (i in 0..3){
            for (j in 0..3){
                gameMatrixArray[i][j] = x
                x++
            }
        }

        image = findViewById(R.id.image1)

        image?.x = 300F
        image?.y = 500F
        image?.tag = "test"


    }

    fun moveBlock(direction: String) {

        moveArray(direction)

        println("Matrix")
        for (i in 0..3){
            for (j in 0..3) {
                print(gameMatrixArray[i][j])
                print(",")
            }
            println()
        }

        println(direction)

        println ("pos x->" +image?.x)
        println ("pos y->" +image?.y)
        println ("pos tag->" + image?.tag)
        println ("pos----------")

        posX = image!!.x
        posY = image!!.y

        when (direction) {
            "up" -> posY += -200
            "down" -> posY += 200
            "left" -> posX += -200
            "right" -> posX += 200

            else -> {
            }
        }

        if (direction == "left" || direction == "right") {

            val animatorX = ObjectAnimator.ofFloat(image, "x", posX).apply {
                duration = 100
                start()
            }
        } else {
            val animatorY = ObjectAnimator.ofFloat(image, "y", posY).apply {
                duration = 100
                start()
            }
        }

    }

    fun moveArray(direction: String) {

        //get position of zero
        var posX = 0
        var posY = 0

        for (i in 0..3){
            for (j in 0..3){
                if (gameMatrixArray[i][j] == 0) {
                    posX = i
                    posY = j
                }
            }
        }
        // println ("Pozicia nuly v poli->" + posX + posY)
        //check direction and position and move if possible
        if (direction == "right"){
            if (posY != 0) {
                gameMatrixArray[posX][posY] = gameMatrixArray[posX][posY-1]
                gameMatrixArray[posX][posY-1] = 0
            }
        }
        if (direction == "left"){
            if (posY != 3) {
                gameMatrixArray[posX][posY] = gameMatrixArray[posX][posY+1]
                gameMatrixArray[posX][posY+1] = 0
            }
        }
        if (direction == "up"){
            if (posX != 3) {
                gameMatrixArray[posX][posY] = gameMatrixArray[posX+1][posY]
                gameMatrixArray[posX+1][posY] = 0
            }
        }
        if (direction == "down"){
            if (posX != 0) {
                gameMatrixArray[posX][posY] = gameMatrixArray[posX-1][posY]
                gameMatrixArray[posX-1][posY] = 0
            }
        }
    }
}