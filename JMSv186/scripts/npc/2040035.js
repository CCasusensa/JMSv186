function action(mode, type, selection) {
	cm.removeAll(4001022);
	cm.removeAll(4001023);
	cm.getPlayer().endPartyQuest(1202);//might be a bad implentation.. incase they dc or something
	cm.gainNX(2500);
	cm.warp(221024500);
	cm.dispose();
}