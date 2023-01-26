const {createApp} = Vue 

const app = createApp({
    data() {
        return {
            data: [],
            dataTitle: [],
            balanceAccount: undefined,
            accountNumberCurrent: '',
            yearStartSelected: '',
            yearEndSelected: '',
        }
    },
    created() {
        this.loadAccount()
    },
    methods: {
        loadAccount() {
            const params = new URLSearchParams(location.search)
            const id = params.get('id')  
            axios.get('/api/accounts/' + id)   
                .then(result => {
                    this.data = result.data.transactions
                    this.dataTitle = result.data
                    this.accountNumberCurrent = this.dataTitle.number
                    this.balanceAccount = this.dataTitle.balance
                    this.data.sort((a, b) => {
                        if(a.id > b.id) {
                            return -1
                        }
                    })
                })
        },
        logOut() {
            axios.post('/api/logout')
                .then(() => location.href = '/web/index.html')
        },
        downloadPDF() {
            axios({
                url: '/api/export-pdf',
                method: 'POST',
                data: {
                    startDate:this.yearStartSelected,
                    endDate:this.yearEndSelected,
                    accountNumber:this.accountNumberCurrent
                },
                responseType: 'blob',
            })
            .then(response => {
                const href = URL.createObjectURL(response.data)
                const link = document.createElement('a')
                link.href = href
                link.setAttribute('download', 'transactions.pdf')
                document.body.appendChild(link)
                link.click()
                Swal.fire(
                    'One moment...download done!',
                    'Look at in the file with your transactions',
                    'success'
                )
            })
            .catch(() => {
                if(this.yearStartSelected > this.yearEndSelected) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'The start date cannot be greater than the end date',
                    })
                }
                else{
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Please, fill both fields',
                    })
                }
            })
        }   
    }
})
app.mount('#content')