package com.example.ludogame

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.view.*
import kotlin.math.min
import kotlin.random.Random

class MainActivity: Activity(){
    override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(LudoView(this))}
}

class LudoView(private val a:Activity):View(a){
    private val p=Paint(Paint.ANTI_ALIAS_FLAG)
    private val C=intArrayOf(Color.rgb(220,40,50),Color.rgb(35,165,75),Color.rgb(245,190,35),Color.rgb(45,105,210))
    private val N=arrayOf("Red","Green","Yellow","Blue")
    private val path=arrayOf(
        1 to 6,2 to 6,3 to 6,4 to 6,5 to 6,6 to 5,6 to 4,6 to 3,6 to 2,6 to 1,
        7 to 1,8 to 1,8 to 2,8 to 3,8 to 4,8 to 5,9 to 6,10 to 6,11 to 6,12 to 6,13 to 6,
        13 to 7,13 to 8,12 to 8,11 to 8,10 to 8,9 to 8,8 to 9,8 to 10,8 to 11,8 to 12,8 to 13,
        7 to 13,6 to 13,6 to 12,6 to 11,6 to 10,6 to 9,5 to 8,4 to 8,3 to 8,2 to 8,1 to 8,
        1 to 7,2 to 7,3 to 7,4 to 7,5 to 7,6 to 7,7 to 7,8 to 7
    )
    private var pos=Array(4){IntArray(4){-1}}
    private var turn=0; private var dice=1; private var rolled=false
    private var msg="Red: roll the dice"
    private val safe=setOf(0,8,13,21,26,34,39,47)

    init{p.typeface=Typeface.DEFAULT_BOLD}

    override fun onDraw(c:Canvas){
        super.onDraw(c);c.drawColor(Color.rgb(248,248,248))
        val s=min(width,height-190f);val l=(width-s)/2f;t(s,l,c)
        board(c,l,70f,s/15f);tokens(c,l,70f,s/15f)
        p.textAlign=Paint.Align.CENTER;p.color=Color.DKGRAY;p.textSize=19f
        c.drawText(msg,width/2f,70+s+30,p)
        p.color=Color.WHITE;c.drawRoundRect(width/2f-45,70+s+48,width/2f+45,70+s+138,18f,18f,p)
        p.style=Paint.Style.STROKE;p.strokeWidth=4f;p.color=Color.DKGRAY
        c.drawRoundRect(width/2f-45,70+s+48,width/2f+45,70+s+138,18f,18f,p);p.style=Paint.Style.FILL
        p.textSize=42f;p.color=Color.BLACK;c.drawText("$dice",width/2f,70+s+108,p)
        p.color=Color.rgb(103,58,183);c.drawRoundRect(width/2f+65,70+s+58,width/2f+215,70+s+128,18f,18f,p)
        p.color=Color.WHITE;p.textSize=18f;c.drawText("ROLL",width/2f+140,70+s+102,p)
    }
    private fun t(s:Float,l:Float,c:Canvas){}
    private fun board(c:Canvas,l:Float,t:Float,cell:Float){
        val rs=arrayOf(0f to 0f,9f to 0f,9f to 9f,0f to 9f)
        for(i in 0..3){p.color=C[i];val(x,y)=rs[i];c.drawRect(l+x*cell,t+y*cell,l+(x+6)*cell,t+(y+6)*cell,p)}
        p.color=Color.WHITE
        for(i in 0..3) for(j in 0..1){ }
        for(i in 0..3){
            val(x,y)=rs[i]
            c.drawCircle(l+(x+2)*cell,t+(y+2)*cell,cell*.72f,p);c.drawCircle(l+(x+4)*cell,t+(y+2)*cell,cell*.72f,p)
            c.drawCircle(l+(x+2)*cell,t+(y+4)*cell,cell*.72f,p);c.drawCircle(l+(x+4)*cell,t+(y+4)*cell,cell*.72f,p)
        }
        p.color=Color.WHITE
        for(r in 6..8)for(col in 0..14)if(!(col in 6..8))c.drawRect(l+col*cell,t+r*cell,l+(col+1)*cell,t+(r+1)*cell,p)
        for(col in 6..8)for(r in 0..14)if(!(r in 6..8))c.drawRect(l+col*cell,t+r*cell,l+(col+1)*cell,t+(r+1)*cell,p)
        p.style=Paint.Style.STROKE;p.color=Color.LTGRAY;p.strokeWidth=1f
        for(i in 0..15){c.drawLine(l+i*cell,t+6*cell,l+i*cell,t+9*cell,p);c.drawLine(l+6*cell,t+i*cell,l+9*cell,t+i*cell,p)}
        p.style=Paint.Style.FILL
        // finish triangle
        val cx=l+7.5f*cell;val cy=t+7.5f*cell
        tri(c,l,t,cell,cx,cy,Color.RED,6f,6f,9f,6f)
        tri(c,l,t,cell,cx,cy,C[1],9f,6f,9f,9f)
        tri(c,l,t,cell,cx,cy,C[2],9f,9f,6f,9f)
        tri(c,l,t,cell,cx,cy,C[3],6f,9f,6f,6f)
    }
    private fun tri(c:Canvas,l:Float,t:Float,cell:Float,cx:Float,cy:Float,col:Int,x:Float,y:Float,x2:Float,y2:Float){
        val q=Path();q.moveTo(cx,cy);q.lineTo(l+x*cell,t+y*cell);q.lineTo(l+x2*cell,t+y2*cell);q.close();p.color=col;c.drawPath(q,p)
    }
    private fun tokens(c:Canvas,l:Float,t:Float,cell:Float){
        val home=arrayOf(arrayOf(2f to 2f,4f to 2f,2f to 4f,4f to 4f),arrayOf(11f to 2f,13f to 2f,11f to 4f,13f to 4f),arrayOf(11f to 11f,13f to 11f,11f to 13f,13f to 13f),arrayOf(2f to 11f,4f to 11f,2f to 13f,4f to 13f))
        for(pl in 0..3)for(k in 0..3){
            val xy=if(pos[pl][k]<0)home[pl][k] else path[(pos[pl][k]+pl*13)%path.size]
            p.color=C[pl];c.drawCircle(l+(xy.first+.5f)*cell,t+(xy.second+.5f)*cell,cell*.32f,p)
            p.color=Color.WHITE;p.style=Paint.Style.STROKE;p.strokeWidth=2f;c.drawCircle(l+(xy.first+.5f)*cell,t+(xy.second+.5f)*cell,cell*.32f,p);p.style=Paint.Style.FILL
        }
    }
    override fun onTouchEvent(e:MotionEvent):Boolean{
        if(e.action!=MotionEvent.ACTION_UP)return true
        val s=min(width,height-190f)
        if(e.y>70+s+40){
            if(!rolled){
                dice=Random.nextInt(1,7);rolled=true
                val movable=(0..3).filter{pos[turn][it]<0 && dice==6 || pos[turn][it]>=0 && pos[turn][it]+dice<=path.size-1}
                if(movable.isEmpty()){msg="${N[turn]} cannot move";nextTurn()}
                else msg="${N[turn]} rolled $dice — tap a token"
            }else{
                val k=(0..3).firstOrNull{pos[turn][it]<0 && dice==6 || pos[turn][it]>=0 && pos[turn][it]+dice<=path.size-1}
                if(k!=null){pos[turn][k]=if(pos[turn][k]<0)0 else pos[turn][k]+dice;capture(turn,k); if(pos[turn].all{it>=path.size-1})msg="${N[turn]} WINS! 🎉"; else if(dice!=6)nextTurn() else {rolled=false;msg="${N[turn]} gets another turn"}}
                invalidate()
            }
        }
        invalidate();return true
    }
    private fun capture(pl:Int,k:Int){
        val at=pos[pl][k];if(at<0)return
        for(o in 0..3)if(o!=pl)for(j in 0..3)if(pos[o][j]>=0 && (pos[o][j]+o*13)%path.size==(at+pl*13)%path.size && (at+pl*13)%path.size !in safe)pos[o][j]=-1
    }
    private fun nextTurn(){turn=(turn+1)%4;rolled=false;msg="${N[turn]}: roll the dice"}
}
