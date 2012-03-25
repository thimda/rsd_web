package nc.uap.lfw.design.itf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BCPManifest implements Serializable{

	private List<BusinessComponent> businessComponentList = new ArrayList<BusinessComponent>();
	
	 public List getBusinessComponentList(){
         return businessComponentList;
    }

    public void setBusinessComponentList(List businessComponentList){
         this.businessComponentList = businessComponentList;
    }


    public void addBusinessComponent(BusinessComponent component){
        if(businessComponentList == null)
            businessComponentList = new ArrayList();
        businessComponentList.add(component);
    }

    public void reomveBusinessComponent(BusinessComponent component){
          if(businessComponentList != null)
        {
            businessComponentList.remove(component);
        }
    }
}
