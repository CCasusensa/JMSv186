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

// �K�V���|�� ���A�A�C�e��
function DoSomething() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0x003F);

	// data
	// 16 = �̌��p�A�o�^�[
	p.write(15);
	p.writeMapleAsciiString("�����~�g : aaaaaaaaa");
	p.writeInt(0);
	//p.writeMapleAsciiString("�����~�g");
	p.writeZeroBytes(100);

	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

function action(mode, type, selection) {
	DoSomething();
	cm.dispose();
}