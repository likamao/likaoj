package top.likamao.likaojcodesendbox.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.List;

public class DockerDemo {

    public static void main(String[] args) throws InterruptedException {

        // 初始化docker客户端
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        PingCmd pingCmd = dockerClient.pingCmd();
        pingCmd.exec();

        String imageName = "nginx:latest";

//         拉取镜像
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(imageName);
        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback(){
            @Override
            public void onNext(PullResponseItem item) {
                System.out.println("下载镜像：" + item.getStatus());
                super.onNext(item);
            }
        };
        pullImageCmd.exec(pullImageResultCallback).awaitCompletion();

        //创建容器
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageName);
        CreateContainerResponse createContainerResponse = createContainerCmd
                .withCmd("echo", "Hello, Docker!")
                .exec();
        System.out.println("创建容器：" + createContainerResponse.getId());
        String containerId = createContainerCmd.exec().getId();

        // 查看容器状态
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for (Container container : containerList) {
            System.out.println("容器状态：" + container.getStatus());
//            dockerClient.removeContainerCmd(containerId).exec();
        }

        class LogContainerResultCallbackAdapter extends ResultCallback.Adapter<Frame> {

            @Override
            public void onNext(Frame item) {
                super.onNext(item);
                System.out.println(item.toString());

            }
        }

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();


        // 日志输出
        dockerClient.logContainerCmd(containerId)
                .withStdOut(true)
                .withStdErr(true)
                .exec(new LogContainerResultCallbackAdapter())
                .awaitCompletion();

//
//        // 删除容器
        ListContainersCmd listNeedRemoveContainersCmd = dockerClient.listContainersCmd();
        List<Container> needRemoveContainerList = listNeedRemoveContainersCmd.withShowAll(true).exec();
        for (Container container : needRemoveContainerList) {
            dockerClient.removeContainerCmd(container.getId()).exec();
        }

        // 删除镜像
//        dockerClient.removeImageCmd(imageName).exec();
    }
}
