import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.SharedAccessBlobPermissions;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;

public class Main {

	public static void main(String[] args) throws Exception {

		CloudBlobContainer container = getCloudBlobContainer("profile-image");

		SharedAccessBlobPolicy sasConstraints = new SharedAccessBlobPolicy();

		sasConstraints.setSharedAccessStartTime(new Date());

		sasConstraints.setSharedAccessExpiryTime(increaseCeilDate(new Date(), 7));

		EnumSet<SharedAccessBlobPermissions> permissions = EnumSet.of(SharedAccessBlobPermissions.READ,

				SharedAccessBlobPermissions.WRITE, SharedAccessBlobPermissions.LIST);

		sasConstraints.setPermissions(permissions);

		System.out.println(container.getUri() + "?" + container.generateSharedAccessSignature(sasConstraints, null));

	}

	private static Date increaseCeilDate(Date date, int increaseDays) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		cal.add(Calendar.DAY_OF_MONTH, increaseDays);

		return cal.getTime();

	}

	private static CloudBlobContainer getCloudBlobContainer(String containerName) {

		CloudBlobContainer container = null;

		try {

			CloudStorageAccount account = CloudStorageAccount.parse(System.getenv("connectionString"));

			CloudBlobClient serviceClient = account.createCloudBlobClient();

			container = serviceClient.getContainerReference(containerName);

			container.createIfNotExists();

		} catch (StorageException | URISyntaxException | InvalidKeyException e) {

			e.printStackTrace();

		}

		return container;

	}

}