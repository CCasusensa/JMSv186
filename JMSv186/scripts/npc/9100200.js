// �p�`���R1
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
				cm.sendOk("�p�`���R�̏���");
				break;
			}
		default:
			break;
	}

	cm.dispose();
}