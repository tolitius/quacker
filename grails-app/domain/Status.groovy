import java.io.Serializable

class Status implements Serializable {

	String	message
	Date	dateCreated
	User	quacker

	static hasMany = [liked: User]

	static constraints = {
		message blank:false, size:1..140
	}

    transient defaultJmsTemplate
	transient afterInsert = {
	    try {
	        defaultJmsTemplate.convertAndSend("queue.quacker", this)
	    }
	    catch( e ) {
	        log.error "could not send JMS message: ${e.message}"
	    }
	}
}

