// �X�s�l��
// https://www.nicovideo.jp/watch/sm12852798

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
				var text = "#b�����̃L�m�R�_�Ђ��܂߁A�����̏�C�A��p�̐��咬#k�ɑ����A#b�^�C�����h�̐���s��#k�ւ̃R�[�X���p�ӏo���Ă��܂��B�e���s�n�ł������F�l�̊y�������s�̂��߂Ɋ撣��܂��B�ł́A�ǂ�����s���Ă݂����ł����H\r\n";
				text += "#b#L" + 1 + "##b" + "����X(��p)" + "#l#k\r\n";
				text += "#b#L" + 2 + "##b" + "��C(����)" + "#l#k\r\n";
				text += "#b#L" + 3 + "##b" + "�L�m�R�_��(���{)" + "#l#k\r\n";
				text += "#b#L" + 4 + "##b" + "����s��(�^�C�����h)" + "#l#k\r\n";
				text += "#b#L" + 5 + "##b" + "�v�R��(����)" + "#l#k\r\n";
				cm.sendSimple(text);
				return;
			}
		case 1:
			{
				switch (selection) {
					case 1:
						{
							cm.warp(740000000, 0);
							break;
						}
					case 2:
						{
							cm.warp(701000000, 0);
							break;
						}
					case 3:
						{
							cm.warp(800000000, 0);
							break;
						}
					case 4:
						{
							cm.warp(500000000, 0);
							break;
						}
					case 5:
						{
							cm.warp(702000000, 0);
							break;
						}
					default:
						break;
				}
				break;
			}
		default:
			break;
	}

	cm.dispose();
}