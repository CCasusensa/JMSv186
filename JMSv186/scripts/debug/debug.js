// Packet Debugging Script

function test(c) {
	var p = c.getOutPacket();
	p.writeShort(0x00AD);
	p.writeInt(0x00007DBC); // �����~�g
	p.writeInt(-1); // �_���[�W
	p.writeZeroBytes(100);
	// ProcessPacket
	c.DebugPacket(p.getPacket());
}

// �l���X�̃��b�Z�[�W
function ShopClose(c) {
	var p = c.getOutPacket();
	// �����n����
	p.writeShort(0x015F);
	// ���鏈��
	p.write(0x0A);
	// ����
	p.write(0);
	// ���b�Z�[�W
	p.write(16);
	/*
		0	=	�Ȃ�
		2	=	�Ȃ�
		4	=	�Ȃ�
		7-13=	�Ȃ�
		15	=	���Ԍo�߂Ŏ����ޏꂳ��܂����B�ē��ꂪ�s�\�ł��B(�t���}�����֎����I�ɔ�΂����@�\)
		16-36	�Ȃ�

		�c�Ƌ���
		1	=	�����ł̓I�[�v���o���܂���B(�o�X���̏ꏊ)
		3	=	���X�����Ă��܂�(�X)
		5	=	�����ޏꂳ��܂����B(�Ǖ�)
		6	=	�������Ԃ��o�߂��A���X���J�����Ƃ��ł��܂��񂵂�(�ꏊ���h�~�̋@�\)
		14	=	�i���͔���؂�ł��B(����؂ꂽ�ꍇ�̋����X)

		�ٗp���l
		17	=	�C�x���g���ɋ󂫂��Ȃ��ƃA�C�e���̓X�g�A�[�o���NNPC�̃v���h���b�N�̂Ƃ���ŒT���ׂ��ł��B�X���܂����H
		18	=	�c�Ǝ��Ԃ��߂��ĕX���܂��B
		20	=	(���b�Z�[�W�_�C�A���O�Ȃ��ŕ���)

		�ٗp���l���u�Ǘ��@
		19	=	�}�b�v���ړ�����A���u�Ǘ��@�g�p���ؒf����܂����B���΂炭�A��ɂ܂����p���������B
	*/
	c.DebugPacket(p.getPacket());
}

// Java����Ă΂��
function debug(c) {
	ShopClose(c);
}