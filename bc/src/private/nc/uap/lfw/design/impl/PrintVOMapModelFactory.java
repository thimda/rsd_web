package nc.uap.lfw.design.impl;

import nc.jdbc.framework.mapping.IMappingMeta;

public class PrintVOMapModelFactory {
	private static final IMappingMeta mappingMeta_Join;
   public static IMappingMeta getPrintRegionVOMapMeta() {
        return (IMappingMeta)mappingMeta_Join;
    }
	static
	{
		mappingMeta_Join = new IMappingMeta(){

			public String[] getAttributes() {
				return new String[]{
						 "ctemplateid", "vdefaultprinter", "bnormalcolor", "vleftnote","ipageheight",
		                    "igridcolor", "vfontname", "bdistotalpagenum", "bdirector", "ibotmargin", 
		                    "ifontsize", "vmidnote", "bdispagenum","itopmargin", "iscale",
		                    "vtemplatename", "ipagewidth", "ileftmargin","fpagination", "pk_corp",
		                    "irightmargin", "ffontstyle", "ipagelocate","ibreakposition", "vrightnote",
		                    "vnodecode", "vtemplatecode", "IModelWidth","IModelHeight", "ibillspace",
		                    "vmodeltype","vprepare1", "vprepare2","extendattr"
				};
			}

			public String[] getColumns() {
				return new String[]{
						 "ctemplateid", "vdefaultprinter", "bnormalcolor", "vleftnote","ipageheight",
		                    "igridcolor", "vfontname", "bdistotalpagenum", "bdirector", "ibotmargin", 
		                    "ifontsize", "vmidnote", "bdispagenum","itopmargin", "iscale",
		                    "vtemplatename", "ipagewidth", "ileftmargin","fpagination", "pk_corp",
		                    "irightmargin", "ffontstyle", "ipagelocate","ibreakposition", "vrightnote",
		                    "vnodecode", "vtemplatecode", "modelwidth","modelheight", "billspace",
		                    "model_type","prepare1", "prepare2","extendattr"
				};
			}

			public String getPrimaryKey() {
				return "ctemplateid";
			}

			public String getTableName() {
				return "pub_print_template";
			}
			
		};

	}
}
