package com.xy.lifemanage.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.xy.lifemanage.bean.Bean;
import com.xy.lifemanage.bean.ORBean;
import com.xy.lifemanage.bean.ProjectBean;
import com.xy.lifemanage.bean.RingBean;
import com.xy.lifemanage.bean.TaskBean;
import com.xy.lifemanage.bean.UserBean;
import com.xy.lifemanage.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by nemo on 2016/5/10 0010.
 */
public class DataModel implements IDataModel {
    @Override
    public void getUserOrganizeData(Context context,UserBean userBean, final IGetDataListener listener) {
        List<String> ors = userBean.getOrganizeId();
        if(null==ors){
            listener.onError("无小组");
        }else {
            BmobQuery<ORBean> query = new BmobQuery<ORBean>();
            query.addWhereContainedIn("objectId", ors);
            query.findObjects(context, new FindListener<ORBean>() {
                @Override
                public void onSuccess(List<ORBean> object) {
                    // TODO Auto-generated method stub
                    listener.onSuccess(object);
                }

                @Override
                public void onError(int code, String msg) {
                    listener.onError(msg);
                }
            });
        }
    }

    @Override
    public void getRingData(Context context, final IGetDataListener listener) {
        BmobQuery<RingBean> query = new BmobQuery<RingBean>();
        query.setLimit(50);
        query.findObjects(context, new FindListener<RingBean>() {
            @Override
            public void onSuccess(List<RingBean> object) {
                // TODO Auto-generated method stub
                listener.onSuccess(object);
            }

            @Override
            public void onError(int code, String msg) {
                listener.onError(msg);
            }
        });
    }

    @Override
    public void addOrganizeData(Context context, final ORBean orBean, final ISaveListener saveListener) {
        orBean.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                saveListener.onSuccess(orBean.getObjectId());
            }

            @Override
            public void onFailure(int i, String s) {
                saveListener.onError(s);
            }
        });
    }

    public void updateOrganizeData(final Context context, final ORBean orBean, final String id, final SaveListener saveListener) {
        if (orBean.getOr_image() != null) {
            BmobFile bmobFile = orBean.getOr_image();
            bmobFile.uploadblock(context, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    orBean.update(context, id, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            saveListener.onSuccess();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            saveListener.onFailure(i, s);
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }

    }

    @Override
    public void addTaskDataToOrg(final Context context, final TaskBean taskBean, final List<String> paths, final String orId, final SaveListener saveListener) {
        searchOrForId(context, orId, new ISaveListener() {
            @Override
            public void onSuccess(final Object org) {
                String id = taskBean.getObjectId();
                final List<String> tasks;
                if (null == ((ORBean) org).getOr_task()) {
                    tasks = new ArrayList<String>();
                } else {
                    tasks = ((ORBean) org).getOr_task();
                }
                String[] s = new String[paths.size()];
                paths.toArray(s);

                BmobFile.uploadBatch(context, s, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                        if(list1.size()==paths.size()) {
                            System.out.println("--"+list.size()+"---"+list1.size());
                            taskBean.setImage(list);
                            taskBean.save(context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    tasks.add(taskBean.getObjectId());
                                    ((ORBean) org).setOr_task(tasks);
                                    ((ORBean) org).update(context, orId, new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            saveListener.onSuccess();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            saveListener.onFailure(i, s);
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                }
                            });
                        }

                    }

                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onError(String msg) {
            }
        });


    }

    @Override
    public void addMemberToOrg(final Context context, final String username, final String orId, final SaveListener saveListener) {
        BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.getObject(context, orId, new GetListener<ORBean>() {
            @Override
            public void onSuccess(ORBean orBean) {
                List<String> members = orBean.getOr_member();
                if (members != null && members.size() > 0) {
                    for (int i = 0; i < members.size(); i++) {
                        if (members.get(i).equals(username)) {
                            saveListener.onFailure(1, "你已在该小组中!");
                            return;
                        }
                    }
                } else {
                    members = new ArrayList<String>();
                }
                members.add(username);
                orBean.setOr_member(members);
                orBean.update(context, orId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        saveListener.onSuccess();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        saveListener.onFailure(i, s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println(s);
            }
        });
    }

    @Override
    public void addProjectDataToOrg(final Context context, final ProjectBean projectBean, final List<String> paths, final String orId, final SaveListener saveListener) {
        searchOrForId(context, orId, new ISaveListener() {
            @Override
            public void onSuccess(final Object org) {
                System.out.println("");
                String id = projectBean.getObjectId();
                final List<String> projects;
                if (null == ((ORBean) org).getOr_project()) {
                    projects = new ArrayList<String>();
                } else {
                    projects = ((ORBean) org).getOr_project();
                }
                final List<BmobFile> bmobFiles = new ArrayList<BmobFile>();
                for (int i = 0; i < paths.size(); i++) {
                    BmobFile bmobFile = new BmobFile(new File(paths.get(i)));
                    bmobFiles.add(bmobFile);
                }
                String[] s = new String[paths.size()];
                paths.toArray(s);

                BmobFile.uploadBatch(context, s, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                        if(list1.size()==list.size()) {
                            projectBean.setImages(bmobFiles);
                            projectBean.save(context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    projects.add(projectBean.getObjectId());
                                    ((ORBean) org).setOr_project(projects);
                                    ((ORBean) org).update(context, orId, new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            saveListener.onSuccess();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            saveListener.onFailure(i, s);
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                }
                            });
                        }

                    }

                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onError(String msg) {
            }
        });


    }

    @Override
    public void updateTaskData(Context context, TaskBean taskBean, String taskId, String orId, SaveListener saveListener) {

    }

    @Override
    public void addProjectData(Context context, ProjectBean projectBean, String orId, SaveListener saveListener) {

    }

    @Override
    public void updateProjectData(Context context, ProjectBean projectBean, String projectId, String orId, SaveListener saveListener) {

    }

    @Override
    public void searchOrForCode(final Context context, String code, final ISaveListener saveListener) {
        BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.doSQLQuery(context, "select * from ORBean where or_code=" + code + "", new SQLQueryListener<ORBean>() {
            @Override
            public void done(BmobQueryResult<ORBean> bmobQueryResult, BmobException e) {
                saveListener.onSuccess(bmobQueryResult.getResults().get(0).getObjectId());
            }
        });
    }

    @Override
    public void searchOrForId(Context context, String id, final ISaveListener saveListener) {
        BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.getObject(context, id, new GetListener<ORBean>() {
            @Override
            public void onSuccess(ORBean orBean) {
                saveListener.onSuccess(orBean);
            }

            @Override
            public void onFailure(int i, String s) {
                saveListener.onError(s);
            }
        });
    }

    @Override
    public void searchOrForName(Context context, String name, final ISaveListener saveListener) {
        BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.addWhereEqualTo("or_name",name);
        query.findObjects(context, new FindListener<ORBean>() {
            @Override
            public void onSuccess(List<ORBean> list) {
                saveListener.onSuccess(list.get(0));
            }

            @Override
            public void onError(int i, String s) {
                saveListener.onError(s);
            }
        });
    }

    @Override
    public void searchOrTask(Context context, String id, final IGetDataListener listener) {
        BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.getObject(context, id, new GetListener<ORBean>() {
            @Override
            public void onSuccess(ORBean orBean) {
                listener.onSuccess(orBean.getOr_task());
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onError(s);
            }
        });
    }

    @Override
    public void searchOrProject(Context context, String id, final IGetDataListener listener) {
        BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.getObject(context, id, new GetListener<ORBean>() {
            @Override
            public void onSuccess(ORBean orBean) {
                listener.onSuccess(orBean.getOr_project());
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onError(s);
            }
        });
    }

    @Override
    public void getOrAllData(final Context context, final List<String> tasks, final List<String> projects, final List<Bean> beanList, final IGetDataListener listener) {

        if (null != tasks) {
            BmobQuery<TaskBean> query = new BmobQuery<TaskBean>();
            query.addWhereContainedIn("objectId",tasks);
            query.findObjects(context, new FindListener<TaskBean>() {
                @Override
                public void onSuccess(List<TaskBean> list) {
                    for(int i=0;i<list.size();i++) {
                        TaskBean bean = list.get(i);
                        Bean b = new Bean();
                        b.setTitle(bean.getTitle());
                        b.setDetail(bean.getDetail());
                        b.setDate(bean.getCreatedAt());
                        b.setHasFinish(bean.getHasFinish());
                        b.setHasFujian(bean.getHasFujian());
                        beanList.add(b);
                    }
                    if(projects==null){
                        listener.onSuccess(beanList);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    listener.onError(s);
                }
            });
        }
        if (null != projects) {
            BmobQuery<ProjectBean> query = new BmobQuery<ProjectBean>();
            query.addWhereContainedIn("objectId",projects);
            query.findObjects(context, new FindListener<ProjectBean>() {
                @Override
                public void onSuccess(List<ProjectBean> list) {
                    for(int i=0;i<list.size();i++) {
                        ProjectBean bean = list.get(i);
                        Bean b = new Bean();
                        b.setTitle(bean.getTitle());
                        b.setDetail(bean.getDetail());
                        b.setDate(bean.getCreatedAt());
                        b.setHasFinish(false);
                        b.setHasFujian(bean.getHasFujian());
                        beanList.add(b);
                    }
                    listener.onSuccess(beanList);
                }

                @Override
                public void onError(int i, String s) {
                    listener.onError(s);
                }
            });
        }
    }

    @Override
    public void inOrg(Context context, final String username, String id, final ISaveListener saveListener) {
        BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.getObject(context, id, new GetListener<ORBean>() {
            @Override
            public void onSuccess(ORBean orBean) {
                List<String> members = orBean.getOr_member();
                if (members.contains(username)) {
                    saveListener.onSuccess(username);
                } else {
                    saveListener.onError("");
                }
            }

            @Override
            public void onFailure(int i, String s) {
                saveListener.onError(s);
            }
        });
    }

    int j;

    @Override
    public void getMemberForOrId(final Context context, String id, final IGetDataListener<UserBean> listener) {
        final BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.getObject(context, id, new GetListener<ORBean>() {
            @Override
            public void onSuccess(final ORBean orBean) {
                BmobQuery<UserBean> query1 = new BmobQuery<UserBean>();
                query1.addWhereContainedIn("username",orBean.getOr_member());
                query1.findObjects(context, new FindListener<UserBean>() {
                    @Override
                    public void onSuccess(List<UserBean> list) {
                        listener.onSuccess(list);
                    }
                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onError(s);
            }
        });
    }

    @Override
    public void searchUserForName(Context context, String username, final ISaveListener saveListener) {
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereContains("username",username);
        query.findObjects(context, new FindListener<UserBean>() {
            @Override
            public void onSuccess(List<UserBean> list) {
                saveListener.onSuccess(list.get(0));
            }

            @Override
            public void onError(int i, String s) {
                saveListener.onError(s);
            }
        });
    }

    @Override
    public void updateUserImg(final Context context, final UserBean userBean, Bitmap bitmap, final ISaveListener saveListener) {
        final BmobFile bmobFile = new BmobFile(new File(Utils.toFile(bitmap).getAbsolutePath()));
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                userBean.setImg(bmobFile);
                userBean.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        saveListener.onSuccess("更新成功!");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        saveListener.onSuccess(s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    @Override
    public void updateUser(Context context, UserBean upBean, final ISaveListener saveListener) {
        upBean.update(context,upBean.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                saveListener.onSuccess("");
            }

            @Override
            public void onFailure(int i, String s) {
                saveListener.onError(s);
            }
        });
    }

    @Override
    public void addRing(final Context context, final RingBean ringBean, final List<String> path, final String objectId, final SaveListener saveListener) {
        final String[] s = new String[path.size()];
        for (int i=0;i<path.size();i++){
            s[i] = path.get(i);
        }

        BmobQuery<ORBean> query = new BmobQuery<ORBean>();
        query.getObject(context, objectId, new GetListener<ORBean>() {
            @Override
            public void onSuccess(final ORBean orBean) {
                BmobFile.uploadBatch(context, s, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> list1) {
                        if(list.size()==path.size()) {
                            ringBean.setRingList(list);
                            final BmobFile img = orBean.getOr_image();
                            ringBean.setRingImg(img);
                            ringBean.save(context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    saveListener.onSuccess();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    saveListener.onFailure(i, s);
                                }
                            });
                        }
                    }
                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {
                    }
                    @Override
                    public void onError(int i, String s) {
                        saveListener.onFailure(i,s);
                    }
                });

            }

            @Override
            public void onFailure(int i, String s) {
                saveListener.onFailure(i, s);
            }
        });

    }

    @Override
    public void addRingUser(final Context context, final RingBean ringBean, final List<String> path, final SaveListener saveListener) {
        String[] s = new String[path.size()];
        for (int i=0;i<path.size();i++){
            s[i] = path.get(i);
        }
        List<BmobFile> list = new ArrayList<>();
        for (int i=0;i<path.size();i++){
            BmobFile bmobFile = new BmobFile(new File(path.get(i)));
            list.add(bmobFile);
        }
        BmobFile.uploadBatch(context, s, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if(path.size()==list.size()) {
                    ringBean.setRingList(list);
                    ringBean.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            saveListener.onSuccess();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            saveListener.onFailure(i, s);
                        }
                    });
                }
            }
            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                saveListener.onFailure(i,s);
            }
        });
    }

    @Override
    public void addTaskToUser(final Context context, final TaskBean taskBean, String username, final SaveListener saveListener) {
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.findObjects(context, new FindListener<UserBean>() {
            @Override
            public void onSuccess(List<UserBean> list) {
                UserBean userBean = list.get(0);
                System.out.println(userBean);
                String id = taskBean.getObjectId();
                List<String> tasks;
                if(userBean.getTasks()==null){
                    tasks = new ArrayList<String>();
                }else{
                    tasks = userBean.getTasks();
                }
                tasks.add(id);
                userBean.setTasks(tasks);
                userBean.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        saveListener.onSuccess();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        saveListener.onFailure(i,s);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void getAllTask(Context context, final IGetDataListener listener) {
        BmobQuery<TaskBean> query = new BmobQuery<>();
        query.findObjects(context, new FindListener<TaskBean>() {
            @Override
            public void onSuccess(List<TaskBean> list) {
                listener.onSuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }
        });
    }
}

interface IDataModel {
    void getUserOrganizeData(Context context, UserBean userBean,IGetDataListener listener);

    void getRingData(Context context, IGetDataListener listener);

    void addOrganizeData(Context context, ORBean orBean, ISaveListener saveListener);

    void updateOrganizeData(Context context, ORBean orBean, String id, SaveListener saveListener);

    void addTaskDataToOrg(Context context, TaskBean taskBean, List<String> paths, String orId, SaveListener saveListener);

    void addMemberToOrg(Context context, String username, String orId, SaveListener saveListener);

    void addProjectDataToOrg(Context context, ProjectBean projectBean, List<String> paths, String orId, SaveListener saveListener);

    void updateTaskData(Context context, TaskBean taskBean, String taskId, String orId, SaveListener saveListener);

    void addProjectData(Context context, ProjectBean projectBean, String orId, SaveListener saveListener);

    void updateProjectData(Context context, ProjectBean projectBean, String projectId, String orId, SaveListener saveListener);

    void searchOrForCode(Context context, String code, ISaveListener saveListener);

    void searchOrForId(Context context, String id, ISaveListener saveListener);
    void searchOrForName(Context context, String name, ISaveListener saveListener);

    void searchOrTask(Context context, String id, IGetDataListener listener);

    void searchOrProject(Context context, String id, IGetDataListener listener);

    void getOrAllData(Context context, List<String> tasks, List<String> projects, List<Bean> beanList, IGetDataListener listener);

    void inOrg(Context context, String username, String id, ISaveListener saveListener);

    void getMemberForOrId(Context context, String id, IGetDataListener<UserBean> listener);

    void searchUserForName(Context context,String username,ISaveListener saveListener);

    void updateUserImg(Context context, UserBean userBean, Bitmap bitmap,ISaveListener saveListener);

    void updateUser(Context context,UserBean upBean,ISaveListener saveListener);

    void addRing(Context context,RingBean ringBean,List<String> path,String orURL,SaveListener saveListener);
    void addRingUser(Context context,RingBean ringBean,List<String> path,SaveListener saveListener);
    void addTaskToUser(Context context,TaskBean taskBean,String username,SaveListener saveListener);
    void getAllTask(Context context,IGetDataListener listener);
}