package com.rageps.net.sql.clan;

import com.google.gson.Gson;
import com.rageps.content.clanchannel.ClanMember;
import com.rageps.content.clanchannel.ClanRank;
import com.rageps.content.clanchannel.ClanType;
import com.rageps.content.clanchannel.channel.ClanChannel;
import com.rageps.content.clanchannel.content.ClanLevel;
import com.rageps.net.sql.DatabaseTransactionFuture;
import com.rageps.net.sql.TableRepresentation;
import com.rageps.net.sql.statement.NamedPreparedStatement;
import com.rageps.world.entity.item.Item;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.naming.Name;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class ClanLoaderTransaction extends DatabaseTransactionFuture<ObjectArrayList<ClanChannel>> {

    private final String GET_CLAN = "SELECT * FROM clan";

    private final String GET_MEMBERS = "SELECT * FROM clan_members WHERE clan=?";

    Gson gson = new Gson();


    public ClanLoaderTransaction() {
        super(TableRepresentation.WEB);
    }

    @Override
    public ObjectArrayList<ClanChannel> onExecute(Connection connection) {
            try(NamedPreparedStatement statement = NamedPreparedStatement.create(connection, GET_CLAN)) {
                ResultSet rs = statement.executeQuery();

                ObjectArrayList<ClanChannel> channels = new ObjectArrayList<>();

                while(rs.next()) {

                    ClanChannel channel = new ClanChannel();

                    int clanId = rs.getInt("id");
                    int ownerId = rs.getInt("owner_id");
                    String ownerName = rs.getString("owner_name");
                    String clanName = rs.getString("title");
                    String recruitmentUrl = rs.getString("header_url");
                    String clanTag = rs.getString("clan_tag");
                    ClanType type = ClanType.valueOf(rs.getString("type").toUpperCase().replaceAll(" ", "_"));
                    boolean priv = rs.getBoolean("is_private");
                    boolean invite = rs.getBoolean("invite_only");
                    String desc = rs.getString("short_desc");
                    String about = rs.getString("about_clan");
                    Date time = new Date(rs.getLong("date_created") * 1000);
                    String colourTag = rs.getString("clan_color");
                    ClanLevel level = ClanLevel.valueOf(rs.getString("clan_level").toUpperCase().replaceAll(" ", "_"));
                    Item[] showcase = gson.fromJson(rs.getString("showcase"), Item[].class);

                    String showcasedItems = rs.getString("showcase_items");
                    ObjectArrayList<Item> showcaseItems;
                    if(showcasedItems == null || showcasedItems.equals(""))
                        showcaseItems = new ObjectArrayList<>();
                     else
                        showcaseItems = Arrays.stream(gson.fromJson(showcasedItems, Item[].class)).collect(Collectors.toCollection(ObjectArrayList::new));

                    boolean lootshare = rs.getBoolean("lootshare");
                    String password = rs.getString("password");
                    long experience = rs.getLong("experience");
                    ClanRank enterRank = ClanRank.of(rs.getInt("enter_rank"));
                    ClanRank talkRank = ClanRank.of(rs.getInt("talk_rank"));
                    ClanRank manageRank = ClanRank.of(rs.getInt("manage_rank"));
                    rs.getString("current_task");
                    rs.getString("task_history");
                    rs.getString("achievements");
                    int points = rs.getInt("points");

                    channel.details.owner = ownerName;
                    channel.management.name = clanName;
                    channel.management.password = password;
                    channel.details.established = time.toString();
                    channel.management.forum = recruitmentUrl;
                    channel.management.slogan = desc;
                    channel.management.tag = clanTag;
                    channel.details.type = type;
                    channel.management.color = colourTag;
                    channel.details.points = points;
                    channel.details.experience = experience;
                    channel.details.level = level;
                    channel.management.locked = invite;
                    channel.management.lootshare = lootshare;
                    channel.management.setEnterRank(enterRank);
                    channel.management.setManageRank(manageRank);
                    channel.management.setTalkRank(talkRank);
                    channel.showcase.showcase = showcase;
                    channel.showcase.showcaseItems = showcaseItems;


                    try (NamedPreparedStatement statement1 = NamedPreparedStatement.create(connection, GET_MEMBERS)) {
                        statement1.setInt(1, clanId);

                        ResultSet rs1 = statement1.executeQuery();
                        while (rs1.next()) {
                            int memberId = rs1.getInt("user_id");
                            String username = rs1.getString("username");
                            String rank = rs1.getString("rank");
                            int kills = rs1.getInt("npc_kills");
                            long expGained = rs1.getLong("exp_gained");
                            int totalLevel = rs1.getInt("total_level");
                            String joined = new Date(rs1.getLong("joined") * 1000).toString();
                            ClanMember member = new ClanMember(username);
                            member.joined = joined;
                            member.rank = ClanRank.MEMBER;//todo
                            member.expGained = expGained;
                            member.npcKills = kills;
                            member.playerKills = kills;
                            member.totalLevel = totalLevel;
                            channel.members.add(member);
                        }
                    }
                    super.LOGGER.info("Loading clan {} with {} members", channel.getName(), channel.getMembers().size());
                    channels.add(channel);
                }
                connection.commit();
                return channels;
            } catch (Exception e) {
                super.LOGGER.warn("Error loading clans!", e);
            }
        return null;
    }
}
