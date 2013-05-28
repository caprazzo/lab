import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class FirstNeoDatabase {

    private static enum RelTypes implements RelationshipType
    {
        SUBSCRIBED,
        PERMITTED,
        ROOM_MEMBER, // + RoomMembershipLevel
        ROOM_PARTICIPANT,
        GROUP_OWNER,
        GROUP_MEMBER
    }

    private static enum RoomMembershipLevel {
        OWNER, ADMIN, MEMBER, OUTCAST
    }

    private static class User {

    }

    private static class Room {

    }

    private static class Member {

    }

    private static class Presence {

    }

    public static void main(String[] args) {
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(".");

        // users
        Node matteo = graphDb.createNode();
        matteo.setProperty("label", "user");
        matteo.setProperty("name", "Teo");

        Node sandy = graphDb.createNode();
        sandy.setProperty("label", "user");
        sandy.setProperty("name", "Johan");

        // rooms
        Node imRoom = graphDb.createNode();
        sandy.setProperty("label", "room");
        imRoom.setProperty("name", "Im Room");




        matteo.createRelationshipTo(sandy, RelTypes.SUBSCRIBED);
        sandy.createRelationshipTo(matteo, RelTypes.PERMITTED);





    }
}
