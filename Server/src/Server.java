import java.net.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Common.Image;
import Common.User;

import java.io.*;

public class Server {

	private HashMap<String, User> _users;
	private List<Image> _images;

	public void start(int portNumber) {
		_users = new HashMap<String, User>();
		_images = new ArrayList<Image>();
		// Load data (users, images)
		// ...

		try (BufferedReader reader = Files.newBufferedReader(FileSystems.getDefault().getPath("Users.txt"))) {
			String name = null, password;
			while ((name = reader.readLine()) != null) {
				password = reader.readLine();
				_users.put(name, new User(name, password));
			}

			System.out.println(_users.size() + " user(s) loaded.");
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		try (BufferedReader reader = Files.newBufferedReader(FileSystems.getDefault().getPath("Images.txt"))) {
			String imageName = null;
			int nThemes;
			String[] themes;
			String uploader;
			String note;
			int width;
			int height;
			int size;
			byte[] imageData;

			while ((imageName = reader.readLine()) != null) {
				nThemes = Integer.parseInt(reader.readLine());
				System.out.println(nThemes);
				themes = new String[nThemes];
				for (int i = 0; i < nThemes; ++i)
					themes[i] = reader.readLine();
				uploader = reader.readLine();
				System.out.println(uploader);
				note = reader.readLine();
				System.out.println(note);
				width = Integer.parseInt(reader.readLine());
				System.out.println(width);
				height = Integer.parseInt(reader.readLine());
				System.out.println(height);
				size = Integer.parseInt(reader.readLine());
				System.out.println(size);
				imageData = new byte[size];

				try (FileInputStream fis = new FileInputStream("Images/" + uploader + "/" + imageName)) {
					fis.read(imageData);
				} catch (IOException x) {
					System.err.format("IOException: %s%n", x);
				}

				_images.add(new Image(imageName, themes, uploader, note, width, height, imageData));
			}

			System.out.println(_images.size() + " image(s) loaded.");
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x.getMessage());
		}

		int port = portNumber;
		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(port);
					Socket clientSocket = serverSocket.accept();
					BufferedReader commandIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());) {

				System.out.println("a client connected");

				try {
					String command = commandIn.readLine();

					if (command == null)
						continue;
					else {
						System.out.println("Command " + command + " received.");
						if (command.equals("SignUp")) {
							out.writeBoolean(true);
							out.flush();
							User newUser = (User) in.readObject();
							// Save user information

							if (_users.containsKey(newUser.getName())) {
								out.writeInt(-1);
								out.flush();
								clientSocket.close();
								continue;
							}

							_users.put(newUser.getName(), newUser);

							try (BufferedWriter writer = Files
									.newBufferedWriter(FileSystems.getDefault().getPath("Users.txt"), CREATE, APPEND)) {
								writer.write(newUser.getName());
								writer.newLine();
								writer.write(newUser.getPassword());
								writer.newLine();

								out.writeInt(1);
								out.flush();
							} catch (IOException x) {
								System.err.format("IOException: %s%n", x);
								out.writeInt(-1);
								out.flush();
							}
						} else if (command.equals("LogIn")) {
							out.writeBoolean(true);
							out.flush();
							User newUser = (User) in.readObject();

							if (!_users.containsKey(newUser.getName())) {
								out.writeInt(-1);
								out.flush();
								clientSocket.close();
								continue;
							}

							if (_users.get(newUser.getName()).checkPassword(newUser.getPassword())) {
								out.writeInt(1);
								out.flush();
							} else {
								out.writeInt(0);
								out.flush();
							}
						} else if (command.equals("UploadImage")) {
							out.writeBoolean(true);
							out.flush();

							Image image = (Image) in.readObject();
							String path = "Images/" + image.get_uploader() + "/";
							if (!Files.exists(Paths.get(path)))
								Files.createDirectories(Paths.get(path));
							try (FileOutputStream writer = new FileOutputStream(path + image.get_imageName())) {
								writer.write(image.get_imageData());

								out.writeInt(1);
								out.flush();

								try (BufferedWriter bw = Files.newBufferedWriter(
										FileSystems.getDefault().getPath("Images.txt"), CREATE, APPEND)) {
									bw.write(image.toString());

									out.writeInt(1);
									out.flush();
								} catch (IOException x) {
									System.err.format("IOException: %s%n", x);
									out.writeInt(-1);
									out.flush();
								}

							} catch (IOException x) {
								System.err.format("IOException: %s%n", x);
								out.writeInt(-1);
								out.flush();
							}
							_images.add(image);

							clientSocket.close();
						} else if (command.equals("FetchImages")) {
							System.out.println("FetchImages");
							out.writeBoolean(true);
							out.flush();

							String user = commandIn.readLine();
							for (int i = 0; i < _images.size(); ++i) {
								if (_images.get(i).get_uploader().equals(user)) {
									out.writeBoolean(true);
									out.flush();
									out.writeUnshared(_images.get(i));
									out.flush();
									if (in.readBoolean())
										continue;
								}
							}

							out.writeBoolean(false);
							out.flush();

						} else if (command.equals("Search")) {
							System.out.println("Search");
							out.writeBoolean(true);
							out.flush();

							String query = commandIn.readLine();
							for (int i = 0; i < _images.size(); ++i) {
								System.out.println(_images.get(i).get_themes().length);
								for (int j = 0; _images.get(i).get_themes() != null
										&& j < _images.get(i).get_themes().length; ++j) {
									if (_images.get(i).get_themes()[j].equals(query)) {
										out.writeBoolean(true);
										out.flush();
										out.writeUnshared(_images.get(i));
										out.flush();
										if (in.readBoolean())
											continue;
									}
								}
							}

							out.writeBoolean(false);
							out.flush();

						} else {
							out.writeBoolean(false);
							out.flush();
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				System.out.println(
						"Exception caught when trying to listen on port " + port + " or listening for a connection");
				System.out.println(e.getMessage());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.start(9000);
	}
}