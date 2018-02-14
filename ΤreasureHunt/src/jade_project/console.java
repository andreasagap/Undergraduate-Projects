/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jade_project;

/**
 *
 *
 */
import jade.lang.acl.ACLMessage;

public class console {
	public static void log(ACLMessage message){
		if(message != null){
			System.out.println("\n" + message.getSender().getLocalName() + "> " + message.getContent());
		}
	}

}