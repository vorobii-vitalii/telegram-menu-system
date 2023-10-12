package lpnu.telegram.menu.system.gprc;

import io.grpc.stub.StreamObserver;
import lpnu.telegram.menu.system.CategoryDetails;
import lpnu.telegram.menu.system.CategoryDetailsServiceGrpc;
import lpnu.telegram.menu.system.FetchCategoryDetailsRequest;

public class CategoryDetailsServiceImpl extends CategoryDetailsServiceGrpc.CategoryDetailsServiceImplBase {

    @Override
    public void fetchMenuCategoryDetails(
            FetchCategoryDetailsRequest request,
            StreamObserver<CategoryDetails> responseObserver
    ) {

    }
}
