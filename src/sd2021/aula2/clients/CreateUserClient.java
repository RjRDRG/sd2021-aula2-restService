package sd2021.aula2.clients;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import sd2021.aula2.api.User;
import sd2021.aula2.api.service.RestUsers;
import sd2021.aula2.discovery.Discovery;
import sd2021.aula2.server.UsersServer;

public class CreateUserClient {

	public static void main(String[] args) throws IOException {
		
		if( args.length != 4) {
			System.err.println( "Use: java sd2021.aula2.clients.CreateUserClient userId fullName email password");
			return;
		}

		Discovery discovery = new Discovery( "CreateUsersClient", "http://" + InetAddress.getLocalHost().getHostAddress());
		discovery.startCollectingAnnouncements();

		String serverUrl = discovery.knownUrisOf(UsersServer.SERVICE).iterator().next().toString();
		System.out.println(serverUrl);
		String userId = args[0];
		String fullName = args[1];
		String email = args[2];
		String password = args[3];

		User u = new User( userId, fullName, email, password);

		System.out.println("Sending request to server.");

		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);

		WebTarget target = client.target( serverUrl ).path( RestUsers.PATH );

		Response r = target.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(u, MediaType.APPLICATION_JSON));

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() )
			System.out.println("Success, created user with id: " + r.readEntity(String.class) );
		else
			System.out.println("Error, HTTP error status: " + r.getStatus() );

	}
	
}
