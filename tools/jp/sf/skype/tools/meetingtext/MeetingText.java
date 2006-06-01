package jp.sf.skype.tools.meetingtext;

import jp.sf.skype.Application;
import jp.sf.skype.ApplicationListener;
import jp.sf.skype.ChatMessage;
import jp.sf.skype.ChatMessageAdapter;
import jp.sf.skype.ChatMessageListener;
import jp.sf.skype.Skype;
import jp.sf.skype.SkypeException;
import jp.sf.skype.Stream;

/**
 * �A�v���P�[�V�����̋N���N���X���`���܂��B
 */
public final class MeetingText {
    /**
     * �A�v���P�[�V�������N�����܂��B
     * 
     * @param args
     *            �N���������w�肵�܂��B
     */
    public static void main(String[] args) throws Exception {
        Skype.setDebug(true);
        Skype.setDeamon(false);
        final Application application = Skype.addApplication("conference");
        application.addApplicationListener(new ApplicationListener() {
            public void disconnected(Stream stream) {
            }

            public void connected(final Stream stream) {
                new Thread() {
                    public void run() {
                        try {
                            new MeetingTextShell(stream).open();
                        } catch (Exception e) {
                            Utils.openErrorMessageDialog("�N���Ɏ��s",
                                    "�~�[�e�B���O�e�L�X�g�c�[���̋N���Ɏ��s���܂����B\n����: "
                                            + e.getMessage());
                        }
                    }
                }.start();
            }
        });
        Skype.addChatMessageListener(new ChatMessageAdapter() {
            public void chatMessageReceived(final ChatMessage message) {
                try {
                    if (message.getContent().contains("�u��c�v")) {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    application.connect(Skype.getContactList()
                                            .getFriend(message.getSenderId()));
                                } catch (Exception e) {
                                    Utils
                                            .openErrorMessageDialog("�ڑ��Ɏ��s",
                                                    "���肪�~�[�e�B���O�e�L�X�g�c�[�����C���X�g�[�����Ă��Ȃ����ߐڑ��ł��܂���ł����B");
                                }
                            }
                        }.start();
                    } else if (message.getContent().contains("�u�I���v")) {
                        Skype.setDeamon(true);
                    }
                } catch (final SkypeException e) {
                    new Thread() {
                        @Override
                        public void run() {
                            Utils.openErrorMessageDialog("�v���O�C���G���[",
                                    "�v���O�C���̓����G���[���������܂����B\n����: "
                                            + e.getMessage());
                        };
                    }.start();
                }
            }
        });
        if (args.length == 1) {
            Skype.getContactList().getFriend(args[0]).send("�u��c�v");
        }
    }
}
