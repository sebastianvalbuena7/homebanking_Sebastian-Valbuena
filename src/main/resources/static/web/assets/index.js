const {createApp} = Vue 

const app = createApp({
    data() {
        return {
            password: undefined,
            email: undefined,
            message: '',
            status: undefined,
            lastNameRegister: undefined,
            nameRegister: undefined,
            emailRegister: undefined,
            passwordRegister: undefined
        }
    },
    methods: {
        login() {
            if(this.email !== undefined || this.password !== undefined) {
                axios.post('/api/login',`email=${this.email}&password=${this.password}`)
                .then(() => {
                    axios.get('/api/clients')
                        .then(() => location.href = '../../manager.html')
                        .catch(() => location.href = '/web/accounts.html')
                })
                .catch(() => {
                    if((this.password === undefined || this.password == '') && (this.email == undefined || this.email == '')) {
                        this.message = 'You must fill in all the fields'
                        setTimeout(() => this.message = '', 3000)
                    } else if(this.email == undefined || this.email == '') {
                        this.message = 'You must enter an email'
                        console.log(this.message)
                        setTimeout(() => this.message = '', 3000)
                    } else if(this.password === undefined || this.password == '') {
                        this.message = 'You must enter an password'
                        setTimeout(() => this.message = '', 3000)
                    } else {
                        this.message = 'There is no user with these credentials'
                        setTimeout(() => this.message = '', 3000)
                    }
                })
            }
        },
        register() {
            if(this.nameRegister !== undefined && this.lastNameRegister !== undefined && this.emailRegister !== undefined && this.passwordRegister !== undefined) {
                if(this.emailRegister.includes("@")) {
                    axios.post('/api/clients',`firstName=${this.nameRegister}&lastName=${this.lastNameRegister}&email=${this.emailRegister}&password=${this.passwordRegister}`)
                    .then(() => {
                        this.email = this.emailRegister
                        this.password = this.passwordRegister
                        this.login()
                    })
                    .catch(() => {
                        if(this.nameRegister == undefined || this.nameRegister == '') {
                            this.message = 'You must enter an name'
                            setTimeout(() => this.message = '', 3000)
                        } else if(this.lastNameRegister == undefined || this.lastNameRegister == '') {
                            this.message = 'You must enter an lastName'
                            setTimeout(() => this.message = '', 3000)
                        } else if(this.passwordRegister == undefined || this.passwordRegister == '') {
                            this.message = 'You must enter an password'
                            setTimeout(() => this.message = '', 3000)
                        } else {
                            this.message = 'Data invalid'
                            setTimeout(() => this.message = '', 3000)
                        }
                    })
                }
                else {
                    this.message = 'Not a valid email'
                    setTimeout(() => this.message = '', 3000)
                }
            } else {
                this.message = 'You must fill in all the fields'
                setTimeout(() => this.message = '', 3000)
            }
        }
    }
})
app.mount('#content')