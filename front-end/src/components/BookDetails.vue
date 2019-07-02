<template>
    <div>
        <div v-if="gettingDetails">详细信息加载中……</div>
        <div v-else-if="getDetailsFail">详细信息加载失败……</div>
        <div v-else>
            <div class="row align-items-center">
                <div v-if="admin&&modifierOn" class="row col-12">
                    <div class="col-4">
                        <button v-if="!hasImage" class="pic-item btn btn-outline-secondary">
                            <span>添加图片</span>
                            <input type="file" v-on:change="loadImageBase()"/>
                        </button>
                        <div v-else class="pic-item m-auto">
                            <img v-bind:src="modifyTo.imageBase" class="pic-item" alt="book image"/>
                            <input type="file" v-on:change="loadImageBase()" title="点击更换图片"/>
                        </div>
                    </div>
                    <div class="col-8">
                        <div class="input-group p-1">
                            <span class="input-group-prepend col-2 offset-1">作者</span>
                            <input class="form-control col-8" v-model="modifyTo.author" type="text" placeholder="作者" title="作者"/>
                        </div>
                        <div class="input-group p-1">
                            <span class="input-group-prepend col-2 offset-1">书名</span>
                            <input class="form-control col-8" v-model="modifyTo.title" type="text" placeholder="书名" title="书名"/>
                        </div>
                        <div class="input-group p-1">
                            <span class="input-group-prepend col-2 offset-1">￥</span>
                            <input class="form-control col-8" v-model="modifyTo.price" type="text"
                                   placeholder="单价" title="单价"/>
                        </div>
                        <div class="p-1">ISBN：{{ isbn }}</div>
                        <div v-bind:class="{'text-secondary':stock<=0}">库存剩余：{{ stock }}</div>
                    </div>
                </div>
                <div v-else  class="row col-12">
                    <div class="col-4">
                        <div class="pic-item m-auto">
                            <img v-if="!hasImage" src="@/assets/logo.png" class="pic-item"/>
                            <img v-else v-bind:src="imageBase" class="pic-item" v-bind:alt="title"/>
                        </div>
                    </div>
                    <div class="col-8">
                        <div>{{ author }} - {{ title }}</div>
                        <div>ISBN：{{ isbn }}</div>
                        <div>单价：￥{{ price }}</div>
                        <div v-bind:class="{'text-secondary':stock<=0}">库存剩余：{{ stock }}</div>
                    </div>
                </div>
            </div>
            <div v-if="admin" class="row">
                <div v-if="modifierOn" class="col-10 offset-1">
                    <button class="btn btn-outline-secondary col-5 mt-3"
                            v-on:click="closeBookInfoModifier()">放弃修改
                    </button>
                    <button class="btn btn-outline-success col-5 offset-2 mt-3"
                            v-on:click="finishModification()">提交修改
                    </button>
                </div>
                <button v-else class="btn btn-block btn-outline-info col-10 offset-1 mt-3"
                        v-on:click="openBookInfoModifier()">修改图书信息
                </button>
            </div>
            <div class="h4 text-left mt-5">评论</div>
            <hr/>
            <!-- Comments on the book -->
            <div v-if="hasComments">
                <div v-for="(comminfo, index) in comments" v-bind:key="index" class="text-left">
                    {{ comminfo.username }} 于 {{comminfo.date}} ：{{ comminfo.comment }}
                    <hr/>
                </div>
            </div>
            <div v-else>暂无评论；来写下第一条评论吧</div>
            <div class="row ml-0 mr-0 mt-4">
                <input type="text" class="form-control col-10" placeholder="留下你的评论...（多于6字，至多500字）"
                       v-model="intendedComment"/>
                <button class="btn btn-outline-primary col-2" v-on:click="submitComment()">评论</button>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'BookDetails',
        props: ['title', 'author', 'isbn', 'price', 'stock', 'admin', 'hasImage', 'imageBase'],
        created: function () {
            this.getBookDetails();
            this.modifyTo.title = this.title;
            this.modifyTo.author = this.author;
            this.modifyTo.price = this.price.toString();
        },
        computed: {
            hasComments: function () {
                return this.comments.length > 0;
            }
        },
        data: function () {
            return {
                gettingDetails: false,
                getDetailsFail: false,
                modifierOn: false,
                intendedComment: '',
                comments: [],
                modifyTo: {
                    title: '',
                    author: '',
                    price: '0',
                    imageBase: ''
                },
                imageChanged: false
            };
        },
        methods: {
            getBookDetails: function () {
                this.gettingDetails = true;
                this.$http.post(
                    'library/details', {
                        isbn: this.isbn
                    }
                ).then(
                    (resp)=>{
                        this.comments = resp.data.comments;
                        this.getDetailsFail = false;
                        this.gettingDetails = false;
                    }, ()=>{
                        this.getDetailsFail = true;
                        this.gettingDetails = false;
                    }
                );
            },
            submitComment: function () {
                this.$http.post(
                    'library/details/comments/add', {
                        isbn: this.isbn,
                        comment: this.intendedComment
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.intendedComment = '';
                            this.getBookDetails();
                        }
                    }, ()=>{}
                );
            },
            openBookInfoModifier: function () {
                this.modifyTo.title = this.title;
                this.modifyTo.author = this.author;
                this.modifyTo.price = this.price;
                this.modifyTo.imageBase = this.imageBase;
                this.imageChanged = false;
                this.modifierOn = true;
            },
            closeBookInfoModifier: function () {
                this.modifierOn = false;
            },
            finishModification: function () {
                this.$http.post(
                    'library/details/modify', {
                        isbn: this.isbn,
                        title: this.modifyTo.title,
                        author: this.modifyTo.author,
                        price: this.modifyTo.price
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.setBookImage();
                        }
                        this.closeBookInfoModifier();
                    }, ()=>{
                        alert('服务器无响应');
                    }
                );
            },
            setBookImage: function () {
                if(!this.imageChanged){
                    this.$emit('bookinfo-update');
                    this.getBookDetails();
                    return;
                }
                this.$http.post(
                    'library/set_image', {
                        image: this.modifyTo.imageBase
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.$emit('bookinfo-update');
                            this.getBookDetails();
                        }
                    }, ()=>{
                        alert('服务器无响应');
                    }
                );
            },
            loadImageBase: function () {
                let obj = this;
                let event = event || window.event;
                let file = event.target.files[0];
                let reader = new FileReader();
                reader.onload = function(e) {
                    let list = file.name.split('.');
                    let imageType = list[list.length - 1];
                    if(imageType !== 'png' && imageType !== 'jpg' && imageType !== 'jpeg' && imageType !== 'gif'){
                        alert('仅支持.png/jpg/jpeg/gif的图片格式');
                    }else{
                        // get the file binary
                        obj.modifyTo.imageBase = e.target.result;
                        obj.hasImage = true;
                        obj.imageChanged = true;
                    }
                };
                reader.readAsDataURL(file);
            }
        }
    }
</script>

<style scoped>

</style>
