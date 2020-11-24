var editor1;
KindEditor.ready(function(K) {
    editor1 = K.create('textarea[name="description"]',{
            //参数配置
            width : '95%',
            filePostName: "file",
            uploadJson:  baseURL + "local/localUplaod/upload2",
            minHeight: '450',
            resizeType : 0,//禁止拉伸，1可以上下拉伸，2上下左右拉伸
            filterMode: false,//true时过滤HTML代码，false时允许输入任何代码。
            itmes:  [
                'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
                'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                'anchor', 'link', 'unlink', '|', 'about'
            ]
        }

    );
    //  prettyPrint();
});
var editor2;
KindEditor.ready(function(K) {
    editor2 = K.create('textarea[name="rule"]',{
            //参数配置
            width : '95%',
            filePostName: "file",
            uploadJson:  baseURL + "local/localUplaod/upload2",
            minHeight: '450',
            resizeType : 0,//禁止拉伸，1可以上下拉伸，2上下左右拉伸
            filterMode: false,//true时过滤HTML代码，false时允许输入任何代码。
            itmes:  [
                'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
                'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
                'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                'anchor', 'link', 'unlink', '|', 'about'
            ]
        }

    );
    //  prettyPrint();
});

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'crowb/crowbactivity/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '名称', name: 'title', index: 'title', width: 100 },
			{ label: '需要积分值', name: 'score', index: 'score', width: 100 },
			{ label: '活动人数', name: 'total', index: 'total', width: 80 }, 			
			{ label: '已报名人数', name: 'signed', index: 'signed', width: 80 }, 			
			{ label: '活动开始时间', name: 'starttime', index: 'starttime', width: 80 }, 			
			{ label: '活动结束时间', name: 'endtime', index: 'endtime', width: 130 },
			{ label: '活动状态', name: 'status', width: 130, formatter: function(value, options, row){
                 return value == '0' ?'<span class="label label-success">停用</span>' :
                  (value == '1' ?'<span class="label label-success">启用</span>' :
                  (value == '3' ?'<span class="label label-success">未知</span>' :'未知'))
                                    }},
			{ label: '消息发送状态', name: 'sendStatus', width: 130, formatter: function(value, options, row){
                             return value == '0' ?'<span class="label label-success">未发送</span>' :
                              (value == '1' ?'<span class="label label-success">众筹成功已发送</span>' :
                              (value == '2' ?'<span class="label label-success">众筹失败发送消息</span>' :'未知'))
                                                }},
			/*{ label: '活动介绍描述', name: 'description', index: 'description', width: 80 },
			{ label: '活动规则描述', name: 'rule', index: 'rule', width: 80 }, */
			{ label: '活动入口链接', name: 'link', index: 'link', width: 130 },
			/*{ label: '报名成功后参与地址', name: 'attendAddress', index: 'attend_address', width: 80 },
			{ label: '报名成功后参与时间', name: 'attendTime', index: 'attend_time', width: 80 },*/
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '修改时间', name: 'modifyTime', index: 'modify_time', width: 80 },
			{ label: '是否需要积分支付积分', name: 'isneedScore', width: 60, formatter: function(value, options, row){
                                         return value == '0' ?'<span class="label label-success">不需要</span>' :
                                          (value == '1' ?'<span class="label label-success">需要</span>' :
                                          (value == '2' ?'<span class="label label-success">未知</span>' :'未知'))
                                                            }},
            {header:'操作', name:'操作', width:450, sortable:false, title:false, align:'center', formatter: function(val, obj, row, act){
                            var actions = [];
                                actions.push('<a class="btn btn-primary" onclick="" style="padding: 3px 8px;"><i class="fa fa-pencil-square-o"></i>&nbsp;详情</a>&nbsp;');
                                actions.push('<a class="btn btn-primary" onclick="vm.update('+row.id+')" style="padding: 3px 8px;"><i class="fa fa-pencil-square-o"></i>&nbsp;修改</a>&nbsp;');
                                if(row.status=='1'){
                                     actions.push('<a class="btn btn-primary" onclick="vm.upateStatus('+row.id+',0)" style="padding: 3px 8px;"><i class="fa fa-pencil-square-o"></i>&nbsp;关闭</a>&nbsp;');
                                 }
                                 if(row.status=='0'){
                                      actions.push('<a class="btn btn-primary" onclick="vm.upateStatus('+row.id+',1)" style="padding: 3px 8px;"><i class="fa fa-pencil-square-o"></i>&nbsp;开启</a>&nbsp;');
                                 }
                                 if(row.sendStatus==null || row.sendStatus=='' || row.sendStatus=='0'){
                                    actions.push('<a class="btn btn-primary" onclick="vm.upateSendStatus('+row.id+',1)" style="padding: 3px 8px;"><i class="fa fa-pencil-square-o"></i>&nbsp;众筹成功</a>&nbsp;');
                                 }
                                if(row.sendStatus==null || row.sendStatus=='' || row.sendStatus=='0'){
                                    actions.push('<a class="btn btn-primary" onclick="vm.upateSendStatus('+row.id+',2)" style="padding: 3px 8px;"><i class="fa fa-pencil-square-o"></i>&nbsp;众筹失败</a>&nbsp;');
                                }
                            return actions.join('');
                        }}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        shrinkToFit:false,
        autoScroll: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "scroll" });
        }
    });


     new AjaxUpload('#upload', {
            action: baseURL + "local/localUplaod/upload2",
            name: 'file',
            autoSubmit:true,
            responseType:"json",
            onSubmit:function(file, extension){
                if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
                    alert('只支持jpg、png、gif格式的图片！');
                    return false;
                }
            },
            onComplete : function(file, r){
                console.log("r=="+JSON.stringify(r));
                console.log("file=="+file);
                if(r.code == 0){
                    alert("上传成功!");
                    vm.crowbActivity.bgImg = r.url;
                    // vm.goodsimgshow = imgURL + r.url;
                      console.log("vm.crowbActivity.bgImg=="+vm.crowbActivity.bgImg);
                    //vm.reload();
                }else{
                    alert(r.msg);
                }
            }
    });
});

var vm = new Vue({
	el:'#icloudapp',
	data:{
		showList: true,
		title: null,
		crowbActivity: {
		    bgImg:null,
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.crowbActivity = {};
		},
		update: function (id) {
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    vm.crowbActivity.description=editor1.html();
		    vm.crowbActivity.rule=editor2.html();
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.crowbActivity.id == null ? "crowb/crowbactivity/save" : "crowb/crowbactivity/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.crowbActivity),
                    success: function(r){
                        if(r.code === 0){
                             layer.msg("操作成功", {icon: 1});
                             vm.reload();
                             $('#btnSaveOrUpdate').button('reset');
                             $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
			});
		},
		//更新活动开启  关闭状态
		upateStatus: function (id,status) {
		       debugger;
            var crowbActivity = {
                id:id,
                status:status
            };

            var content = "";
            if(status==1){
                content = "确定要开启该活动?";
            }
            if(status==0){
                content = "确定要关闭该活动?";
            }
            var lock = false;
             layer.confirm(content, {
                 btn: ['确定','取消'] //按钮
             }, function(){
                if(!lock) {
                     lock = true;
                    $.ajax({
                         type: "POST",
                         url: baseURL + "crowb/crowbactivity/update",
                         contentType: "application/json",
                         data: JSON.stringify(crowbActivity),
                         success: function(r){
                             if(r.code == 0){
                                 layer.msg("操作成功", {icon: 1});
                                 $("#jqGrid").trigger("reloadGrid");
                             }else{
                                 layer.alert(r.msg);
                             }
                         }
                    });
                }
              }, function(){
              });
        },
        //更新活动开启  成功或者失败状态
        upateSendStatus: function (id,status) {
            var crowbActivity = {
                id:id,
                sendStatus:status
            };

            var content = "";
            var url = "";
            if(status==1){
                content = "确定要成功处理活动?";
                url = baseURL + "crowb/crowbactivity/successActivity";
            }
            if(status==2){
                content = "确定要失败处理活动?";
                url = baseURL + "crowb/crowbactivity/faireActivity";
            }
            var lock = false;
             layer.confirm(content, {
                 btn: ['确定','取消'] //按钮
             }, function(){
                if(!lock) {
                     lock = true;
                    $.ajax({
                         type: "POST",
                         url: url,
                         contentType: "application/json",
                         data: JSON.stringify(crowbActivity),
                         success: function(r){
                             if(r.code == 0){
                                 layer.msg("操作成功", {icon: 1});
                                 $("#jqGrid").trigger("reloadGrid");
                             }else{
                                 layer.alert(r.msg);
                             }
                         }
                    });
                }
              }, function(){
              });
        },
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "crowb/crowbactivity/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
				    });
			    }
             }, function(){
             });
		},
		getInfo: function(id){
			$.get(baseURL + "crowb/crowbactivity/info/"+id, function(r){
                vm.crowbActivity = r.crowbActivity;
                editor1.html( vm.crowbActivity.description);
                editor2.html(vm.crowbActivity.rule);
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});