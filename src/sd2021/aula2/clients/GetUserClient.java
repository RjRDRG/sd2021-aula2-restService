package sd2021.aula2.clients;

import java.io.IOException;
import java.net.InetAddress;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import sd2021.aula2.api.User;
import sd2021.aula2.api.service.RestUsers;
import sd2021.aula2.discovery.Discovery;
import sd2021.aula2.server.UsersServer;

public class GetUserClient {

	public static void main(String[] args) throws IOException {
		
		if( args.length != 2) {
			System.err.println( "Use: java sd2021.aula2.clients.GetUserClient userId password");
			return;
		}

		Discovery discovery = new Discovery( "GetUserClient", "http://" + InetAddress.getLocalHost().getHostAddress());
		discovery.startCollectingAnnouncements();

		String serverUrl = discovery.knownUrisOf(UsersServer.SERVICE).iterator().next().toString();
		String userId = args[0];
		String password = args[1];
		
		System.out.println("Sending request to server.");
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		
		WebTarget target = client.target( serverUrl ).path( RestUsers.PATH );
		
		Response r = target.path( userId).queryParam("password", password).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			System.out.println("Success:");
			User u = r.readEntity(User.class);
			System.out.println( "User : " + u);
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );

	}
	
}
