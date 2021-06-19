<template>
  <v-container fill-height justify-center>
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-card min-width="350" class="mx-2">
        <v-card-text>
          <v-text-field
            v-model="email"
            :rules="emailRules"
            label="E-mail"
            outlined
            required
          ></v-text-field>

          <v-text-field
            v-model="password"
            :counter="6"
            :rules="passwordRules"
            label="Password"
            outlined
            required
          ></v-text-field>

          <v-card-actions>
            <v-btn text @click="signUp"> Sign Up </v-btn> 
            <v-spacer/>
            <v-btn
              :disabled="!valid"
              color="primary dark"
              @click="submit"
            >
              Login
            </v-btn>
          </v-card-actions>
        </v-card-text>
      </v-card>
    </v-form>
  </v-container>
</template>

<script>
import axios from 'axios'

export default {
  data: () => ({
    valid: true,
    password: "",
    passwordRules: [
      (v) => !!v || "Password is required",
      (v) => (v && v.length >= 6) || "Password must be more than 6 characters",
    ],
    email: "",
    emailRules: [
      (v) => !!v || "E-mail is required",
      (v) => /.+@.+\..+/.test(v) || "E-mail must be valid",
    ]
  }),
  methods: {
    signUp() {
      this.$router.push('/SignUp')
    },
    validate() {
      this.$refs.form.validate();
    },
    submit() {
      this.validate()

      const data = {
        "email": this.email,
        "password": this.password
      }

      const header = {
        'Content-Type': 'application/json'
      }

      axios.post('http://localhost:8084/api/v1/auth/login', data, header)
        .then(response => {
          if (response.status == 200) {
            const authorization = "Bearer " + response.data.accessToken;
            this.$store.commit("setAuthorization", authorization)
            axios.defaults.headers.common['Authorization'] = authorization
            this.$router.push("/register-item")
          } else {
            alert("An incorrect response code is returned")
          }
        })
        .catch(error => {
          if (error.response) {
            alert(error.response.data.error)
            console.log(error.response.data.message)
            // console.log(error.response.status)
            // console.log(error.response.headers)
          } else if (error.request) {
            console.log(error.request)
          } else {
            console.log('Error', error.message)
          }
        })
    }
  },
  mounted() {
    this.$store.commit('setShowBottomNavigation', false)
  },
};
</script>
