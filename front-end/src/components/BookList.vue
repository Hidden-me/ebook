<template>
    <div>
        <!-- Filters -->
        <div>
            <div class="input-group bg-secondary mt-3 mb-3 pt-2 pb-2">
                <!-- Search by title -->
                <input type="text" class="form-control col-7 offset-2" placeholder="书名"
                       v-model="filters.title"/>
                <button class="btn btn-info col-1" v-on:click="requestBookList()">查询</button>
            </div>
            <ul class="nav nav-tabs nav-justified mt-3 mb-3">
                <li class="nav-item" v-for="(ctg, index) in getCategories" v-bind:key="index"
                    v-on:click="switchCategory(index)" v-bind:class="categoryClass(index)">
                    <a>{{ ctg }}</a>
                </li>
            </ul>
        </div>
        <!-- ADMIN: Add new books -->
        <div v-if="admin&&isValidCategory">
            <button class="btn btn-block btn-outline-primary col-10 offset-1 mb-3"
                v-on:click="openBookAdder()">添加新的图书...</button>
            <hr/>
        </div>
        <!-- Books in the selected category -->
        <div>
            <span v-if="gettingBookList">加载书单中……</span>
            <span v-else-if="getBookListFail">获取书单失败……</span>
            <span v-else-if="hasNoCategories">（书单为空）</span>
            <span v-else-if="selectedCategory<0">（在上方选择一个分类）</span>
            <span v-else-if="isEmptyCategory">本分类暂无可供图书~</span>
            <div v-else>
                <BookListItem v-for="(book, index) in getBooks" v-bind:key="index"
                              v-bind:book="book" v-bind:admin="admin" v-on:booklist-update="requestBookList()">
                </BookListItem>
            </div>
        </div>
        <!-- ADMIN: New book form -->
        <div v-if="admin&&bookAdderOn">
            <div class="mask"></div>
            <div class="on-mask bg-white window-md p-4">
                <button class="btn btn-outline-danger col-2 offset-10 mb-3"
                        v-on:click="closeBookAdder()">关闭</button>
                <div class="row align-items-center">
                    <div class="col-4">
                        <button v-if="!hasImage" class="pic-item btn btn-outline-secondary">
                            <span>添加图片</span>
                            <input type="file" v-on:change="loadImageBase()"/>
                        </button>
                        <div v-else class="pic-item m-auto">
                            <img v-bind:src="imageBase" class="pic-item" alt="image to submit" />
                            <input type="file" v-on:change="loadImageBase()" title="点击更换图片"/>
                        </div>
                    </div>
                    <div class="col-8">
                        <div class="input-group p-1">
                            <span class="input-group-prepend col-2 offset-1">ISBN</span>
                            <input class="form-control col-8" v-model="newBook.isbn" type="text"
                                   placeholder="ISBN，创建后不可更改" title="ISBN，创建后不可更改"/>
                        </div>
                        <div class="input-group p-1">
                            <span class="input-group-prepend col-2 offset-1">作者</span>
                            <input class="form-control col-8" v-model="newBook.author" type="text" placeholder="作者" title="作者"/>
                        </div>
                        <div class="input-group p-1">
                            <span class="input-group-prepend col-2 offset-1">书名</span>
                            <input class="form-control col-8" v-model="newBook.title" type="text" placeholder="书名" title="书名"/>
                        </div>
                        <div class="input-group p-1">
                            <span class="input-group-prepend col-2 offset-1">￥</span>
                            <input class="form-control col-8" v-model="newBook.price" type="text"
                                   placeholder="单价" title="单价"/>
                        </div>
                    </div>
                </div>
                <div class="row mt-3">
                    <button class="btn btn-outline-secondary col-3 offset-2">取消</button>
                    <button class="btn btn-outline-success col-3 offset-2" v-bind:disabled="addingNewBook"
                            v-on:click="addNewBook()">添加！</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import BookListItem from '@/components/BookListItem';
    export default {
        name: 'BookList',
        components: {BookListItem},
        props: {
            admin: {
                default: false
            }
        },
        computed: {
            getCategories: function () {
                var catas = [];
                var i;
                for(i = 0; i < this.library.length; i++){
                    catas[i] = this.library[i].category;
                }
                return catas;
            },
            getBooks: function () {
                var index = this.selectedCategory;
                if(index >= 0 && index < this.getCategories.length){
                    return this.library[index].books;
                }
                return [];
            },
            isEmptyCategory: function () {
                if(this.isValidCategory) {
                    return this.library[this.selectedCategory].books.length <= 0;
                }
                return true;
            },
            isValidCategory: function () {
                return this.selectedCategory >= 0 && this.selectedCategory < this.getCategories.length;
            },
            hasNoCategories: function () {
                return this.library.length <= 0;
            },
            getFilters: function () {
                let titles = this.filters.title.split(' ');
                let titleFilters = [];
                for(let i = 0; i < titles.length; i++){
                    if(titles[i].length > 0){
                        titleFilters.push(titles[i]);
                    }
                }
                return {
                    filters: {
                        title: titleFilters
                    }
                };
            }
        },
        created: function() {
            this.requestBookList();
        },
        methods: {
            categoryClass: function (index) {
                return (index === this.selectedCategory) ? 'nav-link active' : 'nav-link';
            },
            switchCategory: function (index) {
                if(index >= 0 && index < this.getCategories.length){
                    this.selectedCategory = index;
                }
            },
            requestBookList: function () {
                this.gettingBookList = true;
                this.$http.post(
                    'library', this.getFilters
                ).then(
                    (resp)=>{
                        this.library = resp.data.library;
                        this.getBookListFail = false;
                        this.gettingBookList = false;
                    }, ()=>{
                        this.library = [];
                        this.getBookListFail = true;
                        this.gettingBookList = false;
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
                        obj.imageBase = e.target.result;
                        obj.hasImage = true;
                    }
                };
                reader.readAsDataURL(file);
            },
            openBookAdder: function () {
                this.bookAdderOn = true;
            },
            closeBookAdder: function () {
                this.bookAdderOn = false;
            },
            addNewBook: function () {
                this.addingNewBook = true;
                this.$http.post(
                    'library/add', this.newBook
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.setNewBookImage();
                        }
                    }, ()=>{
                        alert('服务器无响应');
                        this.addingNewBook = false;
                    }
                );
            },
            setNewBookImage: function () {
                if(!this.hasImage){
                    this.resetBookAdder();
                    this.requestBookList();
                    return;
                }
                this.$http.post(
                    'library/set_image', {
                        image: this.imageBase,
                        type: this.imageType
                    }
                ).then(
                    (resp)=>{
                        if(resp.data.result === 'failure'){
                            alert(resp.data.message);
                        }else{
                            this.resetBookAdder();
                            this.requestBookList();
                        }
                    }, ()=>{
                        alert('服务器无响应');
                        this.addingNewBook = false;
                    }
                );
            },
            resetBookAdder: function () {
                this.closeBookAdder();
                this.imageBase = '';
                this.hasImage = false;
                this.newBook = {
                    isbn: '',
                    title: '',
                    author: '',
                    price: '0'
                };
                this.addingNewBook = false;
            }
        },
        data: function () {
            // set temporarily with fixed data
            // ISBN as the primary key (unique identifier) of a book
            return {
                filters: {
                    title: ''
                },
                library: [],
                gettingBookList: false,
                getBookListFail: false,
                selectedCategory: -1,
                bookAdderOn: false,
                hasImage: false,
                imageBase: '',
                imageType: '',
                newBook: {
                    isbn: '',
                    title: '',
                    author: '',
                    price: '0'
                },
                addingNewBook: false
            };
        }
    }
</script>

<style scoped>

</style>
