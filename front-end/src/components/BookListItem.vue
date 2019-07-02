<template>
    <div class="row align-items-center mb-3">
        <div class="col-2">
            <img v-if="!book.hasImage" src="@/assets/logo.png" class="pic-item"/>
            <img v-else v-bind:src="book.imageBase" class="pic-item" v-bind:alt="title"/>
        </div>
        <div class="col-6">
            <div v-if="refreshing" class="text-center">刷新图书数据……</div>
            <div v-else-if="refreshFail" class="text-center">刷新数据失败……</div>
            <div v-else class="text-center">
                <div>{{ author }} - {{ title }}</div>
                <div>ISBN：{{ book.isbn }}</div>
                <div v-if="noStock&&admin" class="text-danger">库存为空</div>
                <div v-else-if="noStock&&!admin" class="text-secondary">(-_-;) 没库存了</div>
                <div v-else>库存剩余：{{ book.stock }}</div>
            </div>
        </div>
        <div class="col-2">
            <button class="btn btn-outline-info w-100 mb-2" v-on:click="showDetails()">详情</button>
            <button v-if="admin" class="btn btn-outline-success w-100" v-on:click="openStockManager()">库存管理</button>
            <button v-if="!admin" class="btn btn-outline-fav w-100">收藏</button>
        </div>
        <div class="col-2">
            <button v-if="!admin" class="btn btn-outline-primary w-100" v-bind:disabled="noStock"
                    v-bind:title="noStock?'库存为空，去看看其他书吧':''" v-on:click="addToCart()">
                添加到购物车
                <br/>￥{{ book.price }}
            </button>
            <button v-if="admin" class="btn btn-outline-danger w-100" v-on:click="confirmDeletion()">删除</button>
        </div>
        <!-- Detail & mask -->
        <div v-if="detailed">
            <div class="mask"></div>
            <div class="on-mask bg-white window-md p-4">
                <button class="btn btn-outline-danger col-2 offset-10 mb-3"
                        v-on:click="hideDetails()">关闭</button>
                <BookDetails v-bind:title="title" v-bind:author="author" v-bind:isbn="book.isbn"
                        v-bind:stock="book.stock" v-bind:price="book.price" v-bind:admin="admin"
                        v-bind:has-image="book.hasImage" v-bind:image-base="book.imageBase"
                        v-on:bookinfo-update="refreshInfo()"></BookDetails>
            </div>
        </div>
        <!-- Stock manager & mask -->
        <div v-if="admin&&stockManagerOn">
            <div class="mask"></div>
            <div class="on-mask bg-white window-sm p-4">
                <div class="text-center mt-2">
                    <span>设置库存</span>
                </div>
                <div class="input-group col-8 offset-2 mt-2">
                    <div class="input-group-prepend">
                        <button class="btn btn-outline-secondary" v-bind:disabled="isMinStock"
                                v-on:click="decreaseStock()">-</button>
                    </div>
                    <input type="text" class="form-control text-center" v-model.number="book.stock"
                           placeholder="数量"/>
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" v-bind:disabled="isMaxStock"
                                v-on:click="increaseStock()">+</button>
                    </div>
                </div>
                <div class="row mt-3">
                    <button class="btn btn-outline-secondary col-3 offset-2"
                            v-on:click="closeStockManager()">取消</button>
                    <button class="btn btn-outline-success col-3 offset-2"
                            v-on:click="commitStock()">完成</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import BookDetails from '@/components/BookDetails';
    export default {
        name: 'BookListItem',
        components: {BookDetails},
        props: ['book', 'admin'],
        computed: {
            noStock: function () {
                return this.book.stock <= 0;
            },
            author: function () {
                if(this.book.author.length === 0){
                    return '佚名';
                }
                return this.book.author;
            },
            title: function () {
                if(this.book.title.length === 0){
                    return '无题';
                }
                return this.book.title;
            },
            isMaxStock: function () {
                return this.book.stock >= 2147483647;
            },
            isMinStock: function () {
                return this.book.stock <= 0;
            }
        },
        data: function () {
            return {
                detailed: false,
                refreshing: false,
                refreshFail: false,
                stockManagerOn: false
            };
        },
        methods: {
            showDetails: function() {
                this.detailed = true;
            },
            hideDetails: function () {
                this.detailed = false;
            },
            addToCart: function () {
                this.$http.post(
                    'cart/add', {
                        isbn: this.book.isbn
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }
                    }, ()=>{}
                );
            },
            refreshInfo: function () {
                this.refreshing = true;
                this.$http.post(
                    'library/refresh_book', {isbn: this.book.isbn}
                ).then(
                    (resp)=>{
                        this.book = resp.data.book;
                        this.refreshFail = false;
                        this.refreshing = false;
                    }, ()=>{
                        this.refreshFail = true;
                        this.refreshing = false;
                    }
                );
            },
            openStockManager: function () {
                this.stockManagerOn = true;
            },
            closeStockManager: function () {
                this.stockManagerOn = false;
            },
            increaseStock: function () {
                if(!this.isMaxStock){
                    this.book.stock++;
                }
            },
            decreaseStock: function () {
                if(!this.isMinStock){
                    this.book.stock--;
                }
            },
            commitStock: function () {
                this.$http.post(
                    'library/set_stock', {
                        isbn: this.book.isbn,
                        stock: this.book.stock
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.refreshInfo();
                        }
                        this.closeStockManager();
                    }, ()=>{
                        alert('服务器无响应');
                    }
                );
            },
            confirmDeletion: function () {
                let ok = confirm('确定要删除图书《' + this.book.title + '》吗？确认后，该操作将无法撤销！');
                if(ok){
                    this.$http.post(
                        'library/delete', {
                            isbn: this.book.isbn
                        }
                    ).then(
                        (resp)=>{
                            if(resp.data.result === 'failure'){
                                alert(resp.data.message);
                            }else{
                                this.$emit('booklist-update');
                            }
                        }, ()=>{
                            alert('服务器无响应');
                        }
                    );
                }
            }
        }
    }
</script>

<style scoped>

</style>
