//package com.rageps.net.refactor.packet;
//
//import com.google.common.base.CaseFormat;
//import com.rageps.net.packet.IncomingPacket;
//import it.unimi.dsi.fastutil.objects.ObjectArrayList;
//import org.reflections.Reflections;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.Set;
//
//public class PacketConverter {
//
//    public static final ObjectArrayList<PacketDefinition> packets = new ObjectArrayList<>();
//
//    public static void main(String[] args) {
//
//
//        Reflections reflections = new Reflections(com.rageps.net.packet.in.AdvanceDialoguePacket.class.getPackage().getName());
//
//        Set<Class<? extends IncomingPacket>> allClasses = reflections.getSubTypesOf(IncomingPacket.class);
//
//
//        for (Class<? extends IncomingPacket> allClass : allClasses) {
//            PacketDefinition definition = new PacketDefinition(allClass);
//            String name = (stripPackage(allClass.getName()));
//            definition.setFormattedName(name);
//            packets.add(definition);
//        }
//
//
//        for (PacketDefinition packet : packets) {
//            try {
//
//            File model = new File("./packets/model/"+getPacketName(packet.getFormattedName())+".java");
//            FileOutputStream fos=new FileOutputStream(model, true);
//            String str= "" +
//                    "package com.rageps.net.refactor.packet.in.model;\n" +
//                    "\n" +
//                    "import com.rageps.net.refactor.packet.Packet;\n" +
//                    "\n" +
//                    "/**\n" +
//                    " * @author Tamatea <tamateea@gmail.com>\n" +
//                    " */\n" +
//                    "public class "+getPacketName(packet.getFormattedName()) +
//                    " extends Packet {\n" +
//                    "\n" +
//                    "\npublic "+getPacketName(packet.getFormattedName()) +"() {}"+
//                    "\n" +
//                    "\n" +
//                    "\n" +
//                    "\n" +
//                    "\n" +
//                    "}";
//            byte[] b= str.getBytes();
//            fos.write(b);
//            fos.close();
//
//           File encoder = new File("./packets/decoder/"+ getDecoderName(packet.getFormattedName())+".java");
//
//            fos=new FileOutputStream(encoder, true);
//            str =
//                            "package com.rageps.net.refactor.packet.in.decoder;\n" +
//                            "\n" +
//                            "import com.rageps.net.refactor.codec.game.GamePacket;\n" +
//                            "import com.rageps.net.refactor.codec.game.GamePacketReader;\n" +
//                            "import com.rageps.net.refactor.packet.in.PacketDecoder;\n" +
//                            "import com.rageps.net.refactor.packet.in.model."+getPacketName(packet.getFormattedName())+";\n" +
//                            "\n" +
//                            "/**\n" +
//                            " * @author Tamatea <tamateea@gmail.com>\n" +
//                            " */\n" +
//                            "public class "+ getDecoderName(packet.getFormattedName())+" implements PacketDecoder<"+getPacketName(packet.getFormattedName())+"> {\n" +
//                            "\n" +
//                            "    @Override\n" +
//                            "    public "+getPacketName(packet.getFormattedName())+" decode(GamePacket packet) {\n" +
//                            "        GamePacketReader reader = new GamePacketReader(packet);\n" +
//                            "        return new "+getPacketName(packet.getFormattedName())+"();\n" +
//                            "    }\n" +
//                            "}\n";
//            b= str.getBytes();
//            fos.write(b);
//            fos.close();
//
//           File handler = new File("./packets/handler/"+ getHandlerName(packet.getFormattedName())+".java");
//
//            fos=new FileOutputStream(handler, true);
//            str =
//                            "package com.rageps.net.refactor.packet.in.handler;\n" +
//                            "\n" +
//                            "import com.rageps.net.refactor.packet.in.model."+getPacketName(packet.getFormattedName())+";\n" +
//                            "import com.rageps.net.refactor.packet.PacketHandler;\n" +
//                            "import com.rageps.world.entity.actor.player.Player;\n" +
//                            "\n" +
//                            "/**\n" +
//                            " * @author Tamatea <tamateea@gmail.com>\n" +
//                            " */\n" +
//                            "public class "+ getHandlerName(packet.getFormattedName())+" implements PacketHandler<"+getPacketName(packet.getFormattedName())+"> {\n" +
//                            "\n" +
//                            "    @Override\n" +
//                            "    public void handle(Player player, "+getPacketName(packet.getFormattedName())+" packet) {\n" +
//                            "    }\n" +
//                            "}\n";
//            b= str.getBytes();
//            fos.write(b);
//            fos.close();
//
//
//            //   model.createNewFile();
//              // encoder.createNewFile();
//
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//
//    }
//
//    public static String getPacketName(String name) {
//        return name+"Packet";
//    }
//
//    public static String getDecoderName(String name) {
//        return name+"PacketDecoder";
//    }
//    public static String getHandlerName(String name) {
//        return name+"PacketHandler";
//    }
//
//    public static String stripPackage(String pkg) {
//        return pkg.replaceAll("com.rageps.net.packet.in.", "");
//    }
//
//    public static String getNameCamelCase(String underscored) {
//        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, underscored);
//    }
//
//    public static class PacketDefinition {
//
//        private final Class packetClass;
//
//        private String formattedName;
//
//        public PacketDefinition(Class packetClass) {
//            this.packetClass = packetClass;
//        }
//
//        public Class getPacketClass() {
//            return packetClass;
//        }
//
//        public String getFormattedName() {
//            return formattedName;
//        }
//        public void setFormattedName(String formattedName) {
//            this.formattedName = formattedName;
//        }
//
//        @Override
//        public String toString() {
//            return "PacketDefinition{" +
//                    "packetClass=" + packetClass +
//                    ", formattedName='" + formattedName + '\'' +
//                    '}';
//        }
//    }
//
//
//}
