import faker from 'https://cdnjs.cloudflare.com/ajax/libs/Faker/3.1.0/faker.min.js'


import {post, getPosts, like, comment} from './post-service-run.js';
import {getConfig, shouldPost, shouldLike, shouldComment, shouldFollow} from './config-factory.js';
import {getUser, follow} from './user-service-run.js';


export const options = {
  stages: [
    { target: 100, duration: '20s' },
    { target: 50, duration: '200s' },
    { target: 0, duration: '30s' },
  ],
};



export default function () {
  //get config
  const config = getConfig();
  
  //get a user
  const userId = faker.random.number(config.maxUsers)
  const user = getUser(userId)

  if(shouldPost())
    post(user.id)
    
  let lastId = undefined
  //make the reads
  for(let i = 0; i < config.viewRatio; i++) {
    //get a list of post
    const posts = getPosts(i, lastId)
    
    if(posts == undefined || posts == undefined || posts.length == 0)
      break
    try{
    lastId = posts[posts.length - 1].id
    posts.map((post) => {
      //like the post
      if(shouldLike())
        like(post.id, user.id)
      
      //comment on the post
      if(shouldComment())
        comment(post.id, user.id)

      //follow the post user
      if(shouldFollow())
        follow(user.id, post.userId)
    })
    } catch(e) {
      console.error(e)
      console.error(posts)
    }
    
  }
}