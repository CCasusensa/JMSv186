// �p�`���R3
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
				cm.sendYesNo("�p�`���R���n�߂܂��傤���B");
				return;
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