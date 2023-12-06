# Branching strategy:

Here are all the information about the branching strategy we will have in this project.

The strategy we will adopt is based on this video : https://www.youtube.com/watch?v=U_IFGpJDbeU
And this article : https://nvie.com/posts/a-successful-git-branching-model/

The strategy that we will adopt is called a feature strategy. In other terms, we will be creating new branches any new features (in our case any new user story) we will develop.

## Organization of the branches

We will have two main branches: main and development

1. Main is the ultimate branch. The one that we will be secure and that will hold the finished phases of the project. 

2. Development will be the branch we will develop things on more often. This branch will absorb our mistakes and does not need to be perfect. from this branch, we will decide if we can push on the main or not (according to how ready we will be).

3. Features will be individual branches that we will have to create to develop user stories.

## How to 

Each time you'll have to work on a new feature, you will enter the command :

```
$ git checkout -b myfeature development
Switched to a new branch "myfeature"
```

Once finished, you'll be able to merge to development by doing this: 
```
$ git checkout development
Switched to branch 'develop'
$ git merge --no-ff myfeature
Updating ea1b82a..05e9557
(Summary of changes)
$ git branch -d myfeature
Deleted branch myfeature (was 05e9557).
$ git push origin development
```

## Our routine

Here the routine to work with git everyday assuming you're qorking with userstory21:

```
git commit -am "Make sure you save your pending changes"
git checkout development
git pull # Or fetch merge like usual into develop
git checkout userstory21
 # git merge origin/userstory21 here if other people might also be working on userstory21.
 # It'll let you know that it's "Out of date" if other people changed userstory21
git merge development
```

## Modifying this file

Feel free to modify this file if you find better ressources, better git commands or anything you can think about.

## Suggestions

Feel free to make suggestions in this section: 



## Git rebase

Git rebase is a way of staying up to date with the latest code in the develop. Here what you do is you will get a copy of the code that is now on develop and merge it to your local branch. The main plus point with rebasing is you can make sure that your code works when pushed to develop so no stress. I suggest rebasing every two days because your code might not work due to someone elses changes. Then we don't have to wait for the last moment and get stressed and resolve the issues earlier.

### Steps

!! pre step: make sure you add all you changes and commit before rebasing 

1. Switch to develop : `git switch develop`

2. Take a pull: `git pull`

3. Switch back to you branch: `git switch feature_your_branch`

4. Rebase : `Git rebase develop`

-------------------------------------------------------------------------------

At this point you could have two outcomes
  If it passes: then you are good to go just run a gradle build and a gradle run
  to see if everything is working and fix the issues.
  Else : follow the next steps

-------------------------------------------------------------------------------

6. fix the conflicts: when fixing conflicts make sure you do not overwrite other peoples work without consulting them (Please ask help if you are having trouble).

7. Add the changes: `git add .`

8. Commit the changes: `git commit -m "fixed conflicts on"`

9. Continue rebase: `git rebase --continue`

Hopefully by now it should be done if it isn't please contact me (Chanuka).