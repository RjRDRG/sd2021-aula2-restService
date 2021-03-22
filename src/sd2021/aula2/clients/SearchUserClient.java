package sd2021.aula2.clients;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import sd2021.aula2.api.User;
import sd2021.aula2.api.service.RestUsers;
import sd2021.aula2.discovery.Discovery;
import sd2021.aula2.server.UsersServer;

public class SearchUserClient {

	public static void main(String[] args) throws IOException {
		
		if( args.length != 1) {
			System.err.println( "Use: java sd2021.aula2.clients.SearchUserClient query");
			return;
		}

		Discovery discovery = new Discovery( "SearchUserClient", "http://" + InetAddress.getLocalHost().getHostAddress());
		discovery.startCollectingAnnouncements();

		String serverUrl = discovery.knownUrisOf(UsersServer.SERVICE).iterator().next().toString();
		String query = args[0];
		
		System.out.println("Sending request to server.");
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		
		WebTarget target = client.target( serverUrl ).path( RestUsers.PATH );
		
		Response r = target.path("/").queryParam("query", query).request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		if( r.getStatus() == Status.OK.getStatusCode() && r.hasEntity() ) {
			List<User> users = r.readEntity(new GenericType<List<User>>() {});
			System.out.println("Success: (" + users.size() + " users)");
			users.stream().forEach( u -> System.out.println( u));
		} else
			System.out.println("Error, HTTP error status: " + r.getStatus() );

	}
	
}
