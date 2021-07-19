// �r��@�l�b�g�J�t�F
// �p�P�b�g�f�o�b�O

// test
function DoSomething_003F() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0x003F);

	// data
	p.write(0x05);
	p.writeMapleAsciiString("�e�X�g");
	p.writeZeroBytes(100);

	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

function DoSomething_A8() {
	var p = cm.getOutPacket();
	var id = cm.getPlayer().getId();
	// header
	p.writeShort(0xA8);

	// data
	p.writeInt(id);
	p.write(1);
	p.write(1);
	//p.writeShort(1); // success
	p.writeShort(0);
	p.write(0);
	p.writeInt(0);

	p.writeZeroBytes(100);

	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

// �~���N���L���[�u
function DoSomething_AC() {
	var p = cm.getOutPacket();
	var id = cm.getPlayer().getId();
	// header
	p.writeShort(0xAC);

	// data
	p.writeInt(id);
	p.writeInt(1);

	//p.writeZeroBytes(100);

	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

// ���[�J�[
function DoSomething_DC() {
	var p = cm.getOutPacket();
	var id = cm.getPlayer().getId();
	// header
	p.writeShort(0xDC);

	// data
	p.write(17);
	p.writeInt(0); // 0 = success, 1 = fail
	cm.DebugPacket(p.getPacket());
}

// ����
function DoSomething_DC() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0x00DC);

	// data
	p.write(08);
	p.writeZeroBytes(100);

	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}


// �l�����b�Z�[�W
function DoSomething_25() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0x25);

	// data
	p.write(15);
	/*
		0	�A�C�e��������ȏ㎝���Ƃ��ł��Ȃ��ł�
		1	?
		2	?
		3	�o���l
		4	SP (�`���b�g)
		5	�l�C�x (�`���b�g)
		6	����
		7	�M���h�|�C���g
		8	?
		9	?
		10	error code 38
		11
		12
		13	error code 38
		14	error code 38
		15 �p�`���R��
		16

	*/

	p.writeInt(1337);
	//p.writeZeroBytes(100);

	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

// �����_��������
function DoSomething_E1() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0xE1);

	// type
	p.write(4);
	/*
		0	����
		1	����
		2	�ʏ�
		3	����
		4	��������Ȃ���
	*/


	p.writeInt(123456789);
	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

// �p�`���R�ʒǉ����s
function DoSomething_F3() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0xF3);

	p.writeInt(123456789);
	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

// �p�`���R�ʒǉ����s
function DoSomething_F4() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0xF4);
	cm.DebugPacket(p.getPacket());
}

// test
function DoSomething() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0xF3);

	p.writeInt(123456789);
	p.writeZeroBytes(100);
	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}


function action(mode, type, selection) {
	DoSomething();
	cm.dispose();
}