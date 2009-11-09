import java.io.Serializable



/**
 * User domain class.
 */
class User implements Serializable {

    static searchable = [only:['userRealName']]

	static transients = ['pass']

	/* On top of generated association added "following: User" */
	static hasMany = [ authorities: Role,
                        following:   User]

	static belongsTo = Role

	/** Username */
	String username
	/** User Real Name*/
	String userRealName
	/** MD5 Password */
	String passwd
	/** enabled */
	boolean enabled

	String email
	boolean emailShow

	/** description */
	String description = ''

	/** plain password to create a MD5 password */
	String pass = '[secret]'

	static constraints = {
		username(blank: false, unique: true)
		userRealName(blank: false)
		passwd(blank: false)
		enabled()
	}
}

