package com.malkinfo.tictac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val buttons: Array<Array<Button?>> = Array<Array<Button?>>(3) { arrayOfNulls<Button>(3) }
    private var player1Turn = true
    private var roundCount = 0
    private var player1Points = 0
    private var player2Points = 0
    private lateinit var texviewP1: TextView
    private lateinit var texviewP2: TextView
    private var playerFirst = ""
    private var playerSecond = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        texviewP1 = findViewById(R.id.text_viewP1)
        texviewP2 = findViewById(R.id.text_viewP2)
        getPlayerName()
        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "button_$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                buttons[i][j]!!.setOnClickListener(this)
            }
        }
        val buttonReset: Button = findViewById(R.id.resetBtn)
        buttonReset.setOnClickListener { resetGame() }
    }
    private fun getPlayerName() {
        val infalter = LayoutInflater.from(this@MainActivity)
        val plView = infalter.inflate(R.layout.player_info,null)
        val player1Name = plView.findViewById<EditText>(R.id.firstPl1)
        val player2Name = plView.findViewById<EditText>(R.id.firstPl2)
        val plDialog = AlertDialog.Builder(this@MainActivity)
        plDialog.setView(plView)
        plDialog.setPositiveButton("Add"){
            dialog,_->
            if (player1Name.text.toString().isEmpty()
                    && player2Name.text.toString().isEmpty()
            ){
                playerFirst = "Player1"
                playerSecond = "Player2"

                texviewP1.text = "$playerFirst : 0"
                texviewP2.text = "$playerSecond : 0"
            }
            else{
                playerFirst = player1Name.text.toString()
                playerSecond = player2Name.text.toString()

                texviewP1.text = "$playerFirst : 0"
                texviewP2.text = "$playerSecond : 0"
            }

        }
        plDialog.setNeutralButton("Cancel"){
            dialog,_->
            playerFirst = "Player1"
            playerSecond = "Player2"

            texviewP1.text = "$playerFirst : 0"
            texviewP2.text = "$playerSecond : 0"
        }
        plDialog.create()
        plDialog.show()
    }

    override fun onClick(v: View) {
        if (!(v as Button).getText().toString().equals("")) {
            return
        }
        if (player1Turn) {
            (v as Button).setText("X")
            v.setTextColor(resources.getColor(R.color.red))
        } else {
            (v as Button).setText("O")
            v.setTextColor(resources.getColor(R.color.green))
        }
        roundCount++
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins()
            } else {
                player2Wins()
            }
        } else if (roundCount == 9) {
            draw()
        } else {
            player1Turn = !player1Turn
        }
    }

    private fun checkForWin(): Boolean {
        val field = Array(3) { arrayOfNulls<String>(3) }
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]!!.text.toString()
            }
        }
        for (i in 0..2) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && field[i][0] != "") {
                return true
            }
        }
        for (i in 0..2) {
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && field[0][i] != "") {
                return true
            }
        }
        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && field[0][0] != "") {
            return true
        }
        return if (field[0][2] == field[1][1] && field[0][2] == field[2][0] && field[0][2] != "") {
            true
        } else false
    }

    private fun player1Wins() {
        player1Points++
        AlertDialog.Builder(this@MainActivity)
                .setTitle("Congratulation !!")
                .setIcon(R.drawable.con)
                .setMessage("Congratulation $playerFirst, you are Win ... !!")
                .create()
                .show()
        updatePointsText()
        resetBoard()
    }

    private fun player2Wins() {
        player2Points++
        AlertDialog.Builder(this@MainActivity)
                .setTitle("Congratulation !!")
                .setIcon(R.drawable.con)
                .setMessage("Congratulation $playerSecond, you are Win ... !!")
                .create()
                .show()
        updatePointsText()
        resetBoard()
    }

    private fun draw() {
        AlertDialog.Builder(this)
                .setTitle("Draw !!")
                .setMessage("The game is drawn, Please Play again...!!")
                .setPositiveButton("ok"){dialog,_->
                    resetBoard()

                }
                .setNeutralButton("Cancel"){dialog,_->resetBoard() }
                .create()
                .show()
    }
    /**ok run it*/

    private fun updatePointsText() {
        texviewP1.text = "Player 1: $player1Points"
        texviewP2.text = "Player 2: $player2Points"
    }

    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]!!.setText("")
            }
        }
        roundCount = 0
        player1Turn = true
    }

    private fun resetGame() {
        player1Points = 0
        player2Points = 0
        updatePointsText()
        resetBoard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("roundCount", roundCount)
        outState.putInt("player1Points", player1Points)
        outState.putInt("player2Points", player2Points)
        outState.putBoolean("player1Turn", player1Turn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        roundCount = savedInstanceState.getInt("roundCount")
        player1Points = savedInstanceState.getInt("player1Points")
        player2Points = savedInstanceState.getInt("player2Points")
        player1Turn = savedInstanceState.getBoolean("player1Turn")
    }
}