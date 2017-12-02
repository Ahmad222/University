# Data mining 2,    Assugnment 2     18/5/2015
# Students:   Ahmad Naser eddin   ,  Toni Mikkola

# Reading the data
data2<-read.csv("playlist_pre2-train_clean.csv",sep=",", fileEncoding="latin1", header = TRUE)
datatest<-read.csv("plylist_pre2_obs_ordered.csv",sep=" ", fileEncoding="latin1", header = TRUE)
data2<-data2[order(data2$USERID),]
data22<-data.frame(USERID=data2$USERID , REQUESTEDURL =data2$REQUESTEDURL)
dataTEST2<-data.frame(USERID=datatest$USERID , REQUESTEDURL =datatest$REQUESTEDURL)
# in the following we are doing this to get the number and IDs of users to be used in the out put
dataTESTNUM<-data.frame(USERID=datatest$USERID)
dataTESTNUM <- unique(dataTESTNUM)
dataTESTNUM <- as.matrix(dataTESTNUM)
#

#### plot : relation between users and number of songs that were listened
Users <- unique(data22[,1])
datapltM <- as.matrix(Users)
plo <- matrix(ncol = 2, nrow = nrow(Users))
for(p in 1:length(Users)) #nrow(Users)
{
  plo[p,1] <- datapltM[p,1]
  
  plo[p,2] <- nrow(data22[which(data22$USERID ==  datapltM[p,1]),])
}
hist(plo, xlab = "User ID",ylab = "Num Songs", main = "")

############# preparing the train binary rating matrix (converting the data frame to binary rating matrix)#############################
t2 <- table(data22$USERID,data22$REQUESTEDURL)
mat2 <- matrix(t2,1968,4748)
cn <- colnames(t2) 
colnames(mat2) <- cn
rn <- rownames(t2)
rownames(mat2) <- rn
RatingMat <- as(mat2, "realRatingMatrix")
RatingMatbinary <- binarize(RatingMat, minRating=1)

# levels of a factor : put thhe levels of the test data set equal the train levels   
#to have the same kind of resulted binary rating mtrix
levels(dataTEST2$REQUESTEDURL) <- levels(data22$REQUESTEDURL)  

#################### preparing the test rating matrix (converting the data frame to binary rating matrix)###############################

t2TEST <- table(dataTEST2$USERID,dataTEST2$REQUESTEDURL)
matTEST <- matrix(t2TEST,nrow(t2TEST),ncol(t2TEST))
cn2 <- colnames(t2TEST) 
colnames(matTEST) <- cn2
rn2 <- rownames(t2TEST)
rownames(matTEST) <- rn2
RatingMatTEST <- as(matTEST, "realRatingMatrix")
RatingMatbinaryTEST <- binarize(RatingMatTEST, minRating=1)

############################################################################################################
######################################  Evaluate and Compare Different Recommenders  #######################

#####################################  Using method CROSS_VALIDATION
scheme_binary <- evaluationScheme(RatingMatbinary, method="cross-validation",train=1,k=5, given=2)

#################################### List of different recommenders 
algorithms_binary <- list("random items" = list(name="RANDOM", param=NULL),"popular items" = list(name="POPULAR", param=NULL),"user-based CF" = list(name="UBCF", param=list(method="Jaccard", nn=50)),"AR conf 5% , sup 0.5%" = list(name="AR", param=list(confidence=0.05, support=0.005)),"item-based CF" = list(name="IBCF", param=list(method="Jaccard")), "AR  conf=0.1  sup= 10/1968" = list(name = "AR", param=list(confidence=0.1, support= (10/nrow(mat2)))))

################################### Evaluate the recommenders
results_binary2 <- evaluate(scheme_binary, algorithms_binary, n=c(1,3,5,10,15,20))
################################### PLOT the result to compare
plot(results_binary2, "prec/rec", annotate=4 , legend="center")
plot(results_binary2,"ROC" , annotate=4 , legend="topleft")

################################ Another way of comparision
getConfusionMatrix(results_binary2[[5]])

####################COMPARE UBCF, IBCF
e <- evaluationScheme(RatingMatbinary, method="split", train=0.9, given=2)
rU <- Recommender(getData(e, "train"), "UBCF",param=list(method="Jaccard", nn=50))
rI <- Recommender(getData(e, "train"), "IBCF",param=list(method="Jaccard"))

p1 <- predict(rU, getData(e, "known"), type="topNList")
p2 <- predict(rI, getData(e, "known"), type="topNList")

w1 <-calcPredictionAccuracy(p1, getData(e, "unknown"),given=2)
w2 <-calcPredictionAccuracy(p2, getData(e, "unknown"), given =2)
error <- rbind(w1,w2)
rownames(error) <- c("UBCF","IBCF")
error

getConfusionMatrix(results_binary2[[2]])

############## According to the comparision we decided to use UBCF and with method= cosine, 
##############changing th nn paraameter had not afected 
rUSER <- Recommender(RatingMatbinary, "UBCF",param=list(method="cosine", nn=50))
pUSER <- predict(rUSER, RatingMatbinaryTEST,type="topNList")
liU <- as(pUSER,"list")
liU[[1]]
# out put from method UBCF
output3 <- matrix(ncol=2, nrow=nrow(dataTESTNUM))
for (n in 1:nrow(dataTESTNUM))
{
  #t<-tt2[[n]]
  t<-liU[[n]]
  z<- ""
  for(i in 1:10)
  {
    if(i==1)
    {
      z <- t[i]
    }
    else
    {
      z <- paste(z, t[i], sep = ",")
    }
    
  }
  
  output3[n,1] <- dataTESTNUM[n]
  output3[n,2] <- z
}
colnames(output3) <- c("USER","Suggestions")
output3 <- data.frame(output3)
write.csv(output3, file="Toni-Ahmad_recommendations.csv")



### Number of predictions: 
sum(sapply(liU, length))
24490
########## Since the IBCF was very close we calculated the predictions according to it also

rITEM <- Recommender(RatingMatbinary, "IBCF",param=list(method="Jaccard"))
pITEM <- predict(rITEM, RatingMatbinaryTEST ,type="topNList")
liI <- as(pITEM,"list")
output4 <- matrix(ncol=2, nrow=nrow(dataTESTNUM))
for (n in 1:nrow(dataTESTNUM))
{
  t<-liI[[n]]
  z<- ""
  for(i in 1:10)
  {
    z = paste(z, t[i], sep = ",")
  }
  
  output4[n,1] <- dataTESTNUM[n]
  output4[n,2] <- z
}
colnames(output4) <- c("USER","Suggestions")
output4 <- data.frame(output4)

########### Also those are the recommendatios according to the Association rules using recommenderlab

rAssociation <- Recommender(RatingMatbinary, "AR",param=list(confidence=0.1, support= (10/nrow(mat2))))
pAssociation <- predict(rAssociation, RatingMatbinaryTEST ,type="topNList")
liAs <- as(pAssociation,"list")

##############################################################################################################
## we tried to use the virtual items,  
datas <- data2  #  the train data
datas2 <- data.frame(USERID=datas$USERID , REQUESTEDURL =datas$REQUESTEDURL)
datas3 <- data.frame(USERID=datas$USERID , band_id = paste("DA", datas$band_id, sep = "#"))
datas4 <- data.frame(USERID=datas$USERID , band_id = paste("DA", datas$music_genre, sep = "#"))
data5 <-rbind(datas3,datas4)
colnames(data5) <- c("USER","ITEM")
colnames(datas2) <- c("USER","ITEM")

data5 <-rbind(datas2,data5)
data5 <- data5[order(data5$USER),]
resNew<-caren(data5,min.sup = 0.01,min.conf =0.1,  Bas=T ,prm = T)


ruless<-read.csv("RulesTemp.csv",sep=";", fileEncoding="latin1", header = TRUE)

rules2 <- subset(ruless, substring(ruless$Cons, 1 ,3)!="DA#") # rules without the virtual items

write.csv(rules2, file = "RulesTemp.csv")


PredictionN <- predict.caren(resNew,testss,Bas=T,Top=10)
# After that the results had the #DA prefix which means we had some problems and there is some thing wrong
# we need more time to make it work




#############################################################################################################
#############################################################################################################
## Adding caren to the Recommenderlab#######
recommenderRegistry$delete_entry("CAREN")
.BIN_AR_param <- list(
  support = 0.01, 
  confidence = 0.1,
  maxlen = 2,
  measure = "confidence",
  verbose = FALSE, 
  decreasing = TRUE
)

BIN_AR <- function(data, parameter = NULL) {
  
  ## parameters
  p <- .get_parameters(.BIN_AR_param, parameter)
  
  
  data <- getData.frame(data, ratings=FALSE)
  data<-data[order(data[,1]),]
  
  rule_base <- caren(data, min.sup=0.01, min.conf=0.1, Bas=T ,prm = T)
  #rule_base <- caren(data, parameter=list(min.sup=p$support, min.conf=p$confidence, Bas=T ,prm = T)
  model <- c(list(
    description = "AR: rule base",
    rule_base = rule_base
  ), p 
  ) 
  
  
  
  predict <- function(model, newdata, n=10L, data=NULL, ...) {
  
    
    
    newdata <- getData.frame(newdata, ratings=FALSE)
    newdata <- newdata[order(newdata[,1]),]
    
    ## When we apply the following command we have the result data.frame correctly, we need to change (cast)(extract) 
    ## the topNList from this data frame
    #pre <- predict.caren(rule_base,newdata,Bas=T,Top=10)
    #new("topNList", items = pre , itemLabels = as.character(sapply(dataTESTNUM, as.character)), n = n)
    
    ##  To do that change (data.frame -> topNList ) we tried as follows
    reclist <- list()
    users <- newdata[,1]
    users <- as.numeric(users)
    users <- unique(users)
    p <- predict.caren(rule_base, newdata, Bas=T, Top=n) 
    for(i in 1:length(users))
    {
      row_to_keep <- which(p[,1] == users[i])
      pi <- data.frame(p[row_to_keep,])
      if(nrow(pi) > 0)
      {
        reclist[i] <- as.list(pi[,1])
      }
      else
      {
        reclist[i] <- "NA"
      }
    }
    new("topNList", items = reclist, itemLabels = colnames(newdata), n = n)
  }
  
    
  
  
  ## construct recommender object
  new("Recommender", method = "CAREN", dataType = "binaryRatingMatrix",
      ntrain = nrow(data), model = model, predict = predict)
}

## register recommender
recommenderRegistry$set_entry(
  method="CAREN8", dataType = "binaryRatingMatrix", fun=BIN_AR,
  description="Recommender based on association rules.",
  parameters=.BIN_AR_param
)
recommenderRegistry$get_entry(method="CAREN")

rec_Caren <- Recommender(RatingMatbinary1, method = "CAREN")
pre_Caren <- predict(recRP, RatingMatbinaryTEST, n = 10L)
Res_Caren <- as(pre,"list")

#########################################################################################
#########################################################################################


