
package CommonRespone;

import CommonClass.Profile;


public class SendProfile extends Respone{
    Profile profile;
    
    public SendProfile(Profile profile) {
        super(ResponeType.DONE);
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }
    
    
}
