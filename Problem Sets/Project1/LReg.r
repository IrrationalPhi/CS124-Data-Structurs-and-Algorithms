library(ggplot2)
df <- read.table("dim2.txt", sep = " ")
colnames(df) <- c("n", "average_size", "runtime")
df["log_n"] = log(df["n"])
df["log_size"] = log(df["average_size"])

ggplot(data = df, aes(x = log_n, y = log_size)) + geom_point() +
  geom_smooth(method = "lm") + ggtitle("Plot for dimension 2")
lm1 <- lm(log_size~log_n, data = df)
summary(lm1)

