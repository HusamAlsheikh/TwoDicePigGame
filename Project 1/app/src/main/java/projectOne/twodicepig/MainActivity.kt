package projectOne.twodicepig

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import projectOne.twodicepig.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(){
    //  Array of images
    var imageList = arrayOf(R.drawable.dicesideone, R.drawable.dicesidetwo, R.drawable.dicesidethree, R.drawable.dicesidefour, R.drawable.dicesidefive, R.drawable.dicesidesix)

    //  Variables
    var currentP = 0
    var turnTotal = 0

    //  New Method, View Binding
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  New Method, View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    fun onRollBttn(view: View) {
        //  Random number for dice 1 and dice 2
        val rand1 = (1..6).random()     //  Random number for dice 1
        val rand2 = (1..6).random()     //  Random number for dice 2

        //  Set image of dices to respective numbers
        binding.imageView.setImageResource(imageList[rand1 - 1])
        binding.imageView2.setImageResource(imageList[rand2 - 1])

        //  Get textView
        val turnTotalScore = binding.turnTotal

        //  Get hold button. (To disable if landed double 1's)
        val holdBttn = binding.holdBttn

        if(rand1 == 1 && rand2 == 1){   //  If player lands two 1's
            holdBttn.isEnabled = true

            turnTotal = 0   //  Turn Total score lost
            resetScore()    //  Total Player Score Lost

            currentP = if (currentP == 0) 1 else 0      //  Switch Players
            binding.currentPlayer.text = "Current Player: P${(currentP + 1).toString()}"   //  Update current player
        }
        else if(rand1 == rand2){    //  If player lands doubles (not 1's)
            holdBttn.isEnabled = false  //  Disable button for player to roll again

            turnTotal += rand1 + rand2  //  Add scores to turnTotal

            //  Don't switch players
        }
        else if(rand1 == 1 || rand2 == 1) { //  If player lands a 1
            holdBttn.isEnabled = true   //  Enable button if player rolled again

            //  Scores nothing
            turnTotal = 0

            currentP = if (currentP == 0) 1 else 0      //  Switch Players
            binding.currentPlayer.text = "Current Player: P${(currentP + 1).toString()}"   //  Update current player
        }
        else {  //  Player scored anything else
            holdBttn.isEnabled = true   //  Enable button if player rolled again

            turnTotal += rand1 + rand2  //  Add scores to turnTotal

            //  Continue and don't change players
        }

        turnTotalScore.text = "Turn Total: ${turnTotal.toString()}" //  Update turnTotal Score
    }

    @SuppressLint("SetTextI18n")
    fun onHoldBttn(view: View) {
        addScore(turnTotal) //  Add turnTotal to player

        turnTotal = 0
        binding.turnTotal.text = "Turn Total: ${turnTotal.toString()}" //  Update turnTotal Score

        checkWin()

        currentP = if (currentP == 0) 1 else 0      //  Switch Players
        binding.currentPlayer.text = "Current Player: P${(currentP + 1).toString()}"   //  Update current player
    }

    private fun addScore(score:Int) {
        if(currentP == 0) { //  If player 1
            var p1 = Integer.parseInt(binding.player1Score.text.toString()) //  Get current score num
            p1 += score

            binding.player1Score.text = p1.toString()    //  Update player score
        }
        else {  //  If player 2
            var p2 = Integer.parseInt((binding.player2Score.text.toString()))
            p2 += score

            binding.player2Score.text = p2.toString()    //  Update player score
        }
    }

    //  Reset current players score if they land on double 1's
    private fun resetScore(){
        if(currentP == 0) {
            binding.player1Score.text = "0" //  Total Score Lost
        }
        else {
            binding.player2Score.text = "0" //  Total Score Lost
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkWin(){
        val p1 = Integer.parseInt(binding.player1Score.text.toString()) //  Get current score num
        val p2 = Integer.parseInt(binding.player2Score.text.toString()) //  Get current score num

        if(p1 >= 50 || p2 >= 50){
            val builder = AlertDialog.Builder(this)

            builder.setCancelable(false)
            builder.setMessage("Player ${(currentP + 1).toString()} has won the game! \nPlay Again?")
                .setPositiveButton("Play Again", DialogInterface.OnClickListener{
                    _, _ ->
                    currentP = 0
                    binding.player1Score.text = (0).toString()
                    binding.player2Score.text = (0).toString()
                    currentP = 0
                    turnTotal = 0
                    binding.currentPlayer.text = "Current Player: P1"
                    binding.turnTotal.text = "Turn Total: 0"
                    binding.imageView.setImageResource(imageList[0])
                    binding.imageView2.setImageResource(imageList[0])
                })
            builder.setNegativeButton("Exit", DialogInterface.OnClickListener{
                _, _ -> exitProcess(1 )
                })

            val alert = builder.create()
            alert.show()
        }
    }
}