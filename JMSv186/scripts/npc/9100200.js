// �p�`���R1

function PacketTest() {
	var p = cm.getOutPacket();
	// header
	p.writeShort(0x0168);

	// data
	p.writeInt(7777); // �p�`���R�ʂ̐�
	p.write(0); // ��ԍ� 0, 1, 2
	p.writeZeroBytes(100);

	// ProcessPacket
	cm.DebugPacket(p.getPacket());
}

var status = -1;
function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		status--;
	}

	switch (status) {
		case 0:
			{
				// �p�`���R�ʂ��������Ă���ꍇ
				if (1) {
					cm.sendYesNo("�p�`���R���n�߂܂��傤���B");
					return;
				}

				cm.sendOk("�p�`���R�ʂ�����Ȃ����߁A�p�`���R�����邱�Ƃ��ł��܂���B�����A�|�C���g�V���b�v��ETC�̌o�ϊ������Ńp�`���R�ʂ�̔����ł��̂ŁA�����p���������B");
				break;
			}
		case -1:
			{
				cm.sendOk("�c�O�ł��ˁc�B��Ŏ��Ԃ��ł��܂�����A�����p���������B");
				break;
			}
		case 1:
			{
				PacketTest();
				break;
			}
		default:
			break;
	}

	cm.dispose();
}