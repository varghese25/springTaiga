document.addEventListener('DOMContentLoaded',function() {
    const signupForm = document.querySelector('section');
    signupForm.style.opacity =0;

    setTimeout(() => {
        signupForm.style.transition = 'opacity 1s ease-in-out';
        signupForm.style.opacity =1;
    }, 500);
    });

 // Signup form submit logic
 const submitButton = document.getElementById("submit");
 const signupForm = document.getElementById("signupForm");

 signupForm.addEventListener('submit',(event)=>{
      event.preventDefault();// Prevent the default form submission

      const username = document.getElementById('username').value;
      const password = document.getElementById('password').value;nt
      const confirmPassword = document.getElementById('passwordcon').value;
      const email = document.getElementById('email').value;
      const data ={
        username,
        email,
        password
      }
      
      if(password == confirmPassword){
        const jsonData = JSON.stringify(data);
        fetch('/req/signup', {
          method: 'POST',
          headers:{
            'Content-Type': 'application/json'
          },
          body: jsonData
        })
        .then(response =>{
          //alert('succesfull');
          //if(response.status == 200){
            //alert('succesfull');
          //xs}
        })
      }
      
    })

    
