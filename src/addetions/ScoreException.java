/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addetions;

/**
 *
 * @author DELL
 */
public class ScoreException extends Exception{
   private String message;

    public ScoreException() {
        message="Your Score is Not Enough";
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

   
    
}
