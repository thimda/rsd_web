var $d_v1_grid1 = $ce('DIV');
$d_v1_grid1.style.width = '100%';
$d_v1_grid1.style.height = '100%';
$d_v1_grid1.style.top = '0px';
$d_v1_grid1.style.left = '0px';
$d_v1_grid1.style.position = 'relative';
$d_v1_grid1.id = '$d_v1_grid1';
var $d_v1_um = $ge('$d_v1_um');
$d_v1_um.appendChild($d_v1_grid1);
require('grid', function() {
	var $wd_v1 = pageUI.getWidget('v1');
	var $c_v1_grid1 = new GridComp($d_v1_grid1, "grid1", "0", "0", "100%",
			"100%", "relative", true, false, false, false, null, null, true,
			'', false, false, '1', false, true);
	$c_v1_grid1.widget = $wd_v1;
	$wd_v1.addComponent($c_v1_grid1);
	var model = new GridCompModel();
	var cuserid = new GridCompHeader('cuserid', '�û�����', '120', 'String', true,
			false, true, '', '', 'left', '', false, DefaultRender,
			'StringText', null, null, null, false, false, true);
	model.addHeader(cuserid);
	cuserid.setRequired(true);
	var user_name = new GridCompHeader('user_name', '�û�����', '120', 'String',
			true, false, true, '', '', 'left', '', false, DefaultRender,
			'StringText', null, null, null, false, false, true);
	model.addHeader(user_name);
	var user_code = new GridCompHeader('user_code', '�û�����', '120', 'String',
			true, false, true, '', '', 'left', '', false, DefaultRender,
			'StringText', null, null, null, false, false, true);
	model.addHeader(user_code);
	var user_password = new GridCompHeader('user_password', '�û�����', '120',
			'String', true, false, true, '', '', 'left', '', false,
			DefaultRender, 'StringText', null, null, null, false, false, true);
	model.addHeader(user_password);
	var user_note = new GridCompHeader('user_note', '��ע', '120', 'String',
			true, false, true, '', '', 'left', '', false, DefaultRender,
			'StringText', null, null, null, false, false, true);
	model.addHeader(user_note);
	var pwdlevelcode = new GridCompHeader('pwdlevelcode', '���밲ȫ�������', '120',
			'String', true, false, true, '', '', 'left', '', false,
			ComboRender, 'ComboBox', null, null, null, false, false, true);
	model.addHeader(pwdlevelcode);
	pwdlevelcode.setHeaderComboBoxComboData($cb_v1_combo_cp_user_pwdlevelcode);
	var pwdparam = new GridCompHeader('pwdparam', '�������', '120', 'String',
			true, false, true, '', '', 'left', '', false, DefaultRender,
			'StringText', null, null, null, false, false, true);
	model.addHeader(pwdparam);
	var identityverifycode = new GridCompHeader('identityverifycode', '��֤����',
			'120', 'String', true, false, true, '', '', 'left', '', false,
			ComboRender, 'ComboBox', null, null, null, false, false, true);
	model.addHeader(identityverifycode);
	identityverifycode
			.setHeaderComboBoxComboData($cb_v1_combo_cp_user_identityverifycode);
	var abledate = new GridCompHeader('abledate', '��Ч����', '120', 'UFDate',
			true, false, true, '', '', 'left', '', false, DateRender,
			'DateText', null, null, null, false, false, true);
	model.addHeader(abledate);
	var disabledate = new GridCompHeader('disabledate', 'ʧЧ����', '120',
			'UFDate', true, false, true, '', '', 'left', '', false, DateRender,
			'DateText', null, null, null, false, false, true);
	model.addHeader(disabledate);
	var islocked = new GridCompHeader('islocked', '����', '120', 'UFBoolean',
			true, false, true, '', '', 'center', '', false, BooleanRender,
			'CheckBox', null, null, null, false, false, true);
	model.addHeader(islocked);
	islocked.setValuePair(["Y", "N"]);
	var pk_base_doc = new GridCompHeader('pk_base_doc', '���', '120',
			'SelfDefine', true, false, true, '', '', 'left', '', false,
			DefaultRender, 'StringText', null, null, null, false, false, true);
	model.addHeader(pk_base_doc);
	var base_doc_type = new GridCompHeader('base_doc_type', '�������', '120',
			'Integer', true, false, true, '', '', 'right', '', false,
			ComboRender, 'ComboBox', null, null, null, false, false, true);
	model.addHeader(base_doc_type);
	base_doc_type
			.setHeaderComboBoxComboData($cb_v1_combo_cp_user_base_doc_type);
	var pk_org = new GridCompHeader('pk_org', '������֯', '120', 'String', true,
			true, true, '', '', 'left', '', false, DefaultRender, 'StringText',
			null, null, null, false, false, true);
	model.addHeader(pk_org);
	var pk_org_name = new GridCompHeader('pk_org_name', '������֯', '120',
			'String', true, false, true, '', '', 'left', '', false,
			DefaultRender, 'Reference', null, null, null, false, false, true);
	model.addHeader(pk_org_name);
	pk_org_name.setNodeInfo($rf_v1_refnode_cp_user_pk_org_name);
	var pk_group = new GridCompHeader('pk_group', '��������', '120', 'String',
			true, true, true, '', '', 'left', '', false, DefaultRender,
			'StringText', null, null, null, false, false, true);
	model.addHeader(pk_group);
	var pk_group_name = new GridCompHeader('pk_group_name', '��������', '120',
			'String', true, false, true, '', '', 'left', '', false,
			DefaultRender, 'Reference', null, null, null, false, false, true);
	model.addHeader(pk_group_name);
	pk_group_name.setNodeInfo($rf_v1_refnode_cp_user_pk_group_name);
	var creator = new GridCompHeader('creator', '������', '120', 'String', true,
			true, true, '', '', 'left', '', false, DefaultRender, 'StringText',
			null, null, null, false, false, true);
	model.addHeader(creator);
	var creator_user_name = new GridCompHeader('creator_user_name', '������',
			'120', 'String', true, false, true, '', '', 'left', '', false,
			DefaultRender, 'Reference', null, null, null, false, false, true);
	model.addHeader(creator_user_name);
	creator_user_name.setNodeInfo($rf_v1_refnode_cp_user_creator_user_name);
	var creationtime = new GridCompHeader('creationtime', '����ʱ��', '120',
			'UFDateTime', true, false, true, '', '', 'left', '', false,
			DateTimeRender, 'DateTimeText', null, null, null, false, false,
			true);
	model.addHeader(creationtime);
	var modifier = new GridCompHeader('modifier', '����޸���', '120', 'String',
			true, true, true, '', '', 'left', '', false, DefaultRender,
			'StringText', null, null, null, false, false, true);
	model.addHeader(modifier);
	var modifier_user_name = new GridCompHeader('modifier_user_name', '����޸���',
			'120', 'String', true, false, true, '', '', 'left', '', false,
			DefaultRender, 'Reference', null, null, null, false, false, true);
	model.addHeader(modifier_user_name);
	modifier_user_name.setNodeInfo($rf_v1_refnode_cp_user_modifier_user_name);
	var modifiedtime = new GridCompHeader('modifiedtime', '����޸�ʱ��', '120',
			'UFDateTime', true, false, true, '', '', 'left', '', false,
			DateTimeRender, 'DateTimeText', null, null, null, false, false,
			true);
	model.addHeader(modifiedtime);
	var user_type = new GridCompHeader('user_type', '�û�����', '120', 'Integer',
			true, false, true, '', '', 'right', '', false, ComboRender,
			'ComboBox', null, null, null, false, false, true);
	model.addHeader(user_type);
	user_type.setHeaderComboBoxComboData($cb_v1_combo_cp_user_user_type);
	var pk_usergroupforcreate = new GridCompHeader('pk_usergroupforcreate',
			'�����û���', '120', 'String', true, true, true, '', '', 'left', '',
			false, DefaultRender, 'StringText', null, null, null, false, false,
			true);
	model.addHeader(pk_usergroupforcreate);
	var pk_usergroupforcreate_group_name = new GridCompHeader(
			'pk_usergroupforcreate_group_name', '�����û���', '120', 'String', true,
			false, true, '', '', 'left', '', false, DefaultRender, 'Reference',
			null, null, null, false, false, true);
	model.addHeader(pk_usergroupforcreate_group_name);
	pk_usergroupforcreate_group_name
			.setNodeInfo($rf_v1_refnode_cp_user_pk_usergroupforcreate_group_name);
	var format = new GridCompHeader('format', '���ݸ�ʽ', '120', 'String', true,
			true, true, '', '', 'left', '', false, DefaultRender, 'StringText',
			null, null, null, false, false, true);
	model.addHeader(format);
	var format_name = new GridCompHeader('format_name', '���ݸ�ʽ', '120',
			'String', true, false, true, '', '', 'left', '', false,
			DefaultRender, 'Reference', null, null, null, false, false, true);
	model.addHeader(format_name);
	format_name.setNodeInfo($rf_v1_refnode_cp_user_format_name);
	var isca = new GridCompHeader('isca', 'CA�û�', '120', 'UFBoolean', true,
			false, true, '', '', 'center', '', false, BooleanRender,
			'CheckBox', null, null, null, false, false, true);
	model.addHeader(isca);
	isca.setValuePair(["Y", "N"]);
	var enablestate = new GridCompHeader('enablestate', '����״̬', '120',
			'Integer', true, false, true, '', '', 'right', '', false,
			ComboRender, 'ComboBox', null, null, null, false, false, true);
	model.addHeader(enablestate);
	enablestate.setHeaderComboBoxComboData($cb_v1_combo_cp_user_enablestate);
	var contentlang = new GridCompHeader('contentlang', '��������', '120',
			'String', true, true, true, '', '', 'left', '', false,
			DefaultRender, 'StringText', null, null, null, false, false, true);
	model.addHeader(contentlang);
	var contentlang_dislpayname = new GridCompHeader('contentlang_dislpayname',
			'��������', '120', 'String', true, false, true, '', '', 'left', '',
			false, DefaultRender, 'Reference', null, null, null, false, false,
			true);
	model.addHeader(contentlang_dislpayname);
	contentlang_dislpayname
			.setNodeInfo($rf_v1_refnode_cp_user_contentlang_dislpayname);
	var user_code_q = new GridCompHeader('user_code_q', '�û����루��ѯ��', '120',
			'String', true, false, true, '', '', 'left', '', false,
			DefaultRender, 'StringText', null, null, null, false, false, true);
	model.addHeader(user_code_q);
	var dataoriginflag = new GridCompHeader('dataoriginflag', '�ֲ�ʽ', '120',
			'Integer', true, false, true, '', '', 'right', '', false,
			IntegerRender, 'IntegerText', null, null, null, false, false, true);
	model.addHeader(dataoriginflag);
	var pk_user1 = new GridCompHeader('pk_user1', 'Эͬ�û���ɫ���', '120', 'String',
			true, false, true, '', '', 'left', '', false, DefaultRender,
			'StringText', null, null, null, false, false, true);
	model.addHeader(pk_user1);
	var passwordtrytimes = new GridCompHeader('passwordtrytimes', '���Դ���',
			'120', 'Integer', true, false, true, '', '', 'right', '', false,
			IntegerRender, 'IntegerText', null, null, null, false, false, true);
	model.addHeader(passwordtrytimes);
	var isasyntonc = new GridCompHeader('isasyntonc', '�Ƿ�ͬ����NC', '120',
			'UFBoolean', true, false, true, '', '', 'center', '', false,
			BooleanRender, 'CheckBox', null, null, null, false, false, true);
	model.addHeader(isasyntonc);
	isasyntonc.setValuePair(["Y", "N"]);
	var status = new GridCompHeader('status', 'vostatus', '120', 'Integer',
			true, false, true, '', '', 'right', '', false, IntegerRender,
			'IntegerText', null, null, null, false, false, true);
	model.addHeader(status);
	model.setDataSet(pageUI.getWidget('v1').getDataset('cp_user'));
	window.$c_v1_grid1_model = model;
	var params = {
		widgetid : 'v1',
		uiid : 'grid1',
		eleid : 'grid1',
		type : 'grid'
	};
	new EditableListener($d_v1_grid1, params, 'component');
	if ($d_v1_grid1)
		$d_v1_grid1.style.padding = '0px';
});
if (pageUI.getWidget('v1').getComponent('grid1') != null)
	pageUI.getWidget('v1').getComponent('grid1')
			.setModel(window.$c_v1_grid1_model);

layoutInitFunc();
window.isRenderDone = true;
