// �r��@�l�b�g�J�t�F
// �p�P�b�g�f�o�b�O

// �_���[�W�G�t�F�N�g
function DoSomething_00AD() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0x00AD);

	// data
	p.writeInt(0x00007DBC); // �����~�g
	p.writeInt(-1); // �_���[�W
	p.writeZeroBytes(100);
	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

// test
function DoSomething() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0x00B2);

	// data
	p.writeInt(0x00007DBC); // �����~�g
	p.writeInt(0);
	//p.writeShort(1);
	//p.writeShort(0);
	p.writeZeroBytes(100);
	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

function action(mode, type, selection) {
	DoSomething();
	cm.dispose();
}