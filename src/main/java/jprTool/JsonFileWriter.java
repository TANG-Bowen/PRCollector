package jprTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import com.google.gson.Gson;
import jwrmodel.*;
import prmodel.*;

public class JsonFileWriter {
	
	PRModelBuilder mdBuilder;
	PRModelBuilderBundle mdBuilderBundle;
	StrEBuilder streBuilder;
	PullRequest pr;
	Str_PullRequest str_pr;
	String filePath="";
	File aimf;
	
	public JsonFileWriter(PRModelBuilder mdBuilder)
	{
		this.mdBuilder = mdBuilder;
		this.pr = this.mdBuilder.getPr();
		this.streBuilder = new StrEBuilder(this.pr);
		this.str_pr = this.streBuilder.buildJwrModel();
		this.filePath = this.mdBuilder.getPrDir()+File.separator+this.pr.getRepositoryName()+"_"+this.pr.getId()+"_str.json";
	}
	
	public JsonFileWriter(PRModelBuilderBundle mdBuilderBundle)
	{
		this.mdBuilderBundle = mdBuilderBundle;
		
	}
	
	public JsonFileWriter(PullRequest pr, String filePath)
	{
		this.pr = pr;
		this.streBuilder = new StrEBuilder(this.pr);
		this.str_pr = this.streBuilder.buildJwrModel();
		this.filePath = filePath;
	}
	
	public JsonFileWriter(Path p, int prNum)
	{
		try {
			this.deleteDir(p);
			System.out.println(prNum+"'s directory has been deleted!  ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JsonFileWriter(PullRequest pr)
	{
		this.pr =pr;
	}
	
	public void makeFile()
	{
		Gson gson = new Gson();
		String jsonStr = gson.toJson(str_pr);
		this.aimf = new File(this.filePath);
		
			try {
				FileWriter writer = new FileWriter(this.aimf,false);
				writer.write(jsonStr);
				System.out.println("PR "+this.pr.getId()+"  json file has been written !");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	public void makeFiles()
	{
		if(!this.mdBuilderBundle.getPrmdlBuilders().isEmpty())
		{
			for(PRModelBuilder mdBuilderi : this.mdBuilderBundle.getPrmdlBuilders())
			{
				PullRequest pri = mdBuilderi.getPr();
				StrEBuilder streBuilder = new StrEBuilder(pri);
				Str_PullRequest str_pri = streBuilder.buildJwrModel();
				String filePathi = mdBuilderi.getPrDir()+File.separator+pri.getRepositoryName()+"_"+pri.getId()+"_str.json";
				Gson gsoni = new Gson();
				String jsonStri = gsoni.toJson(str_pri);
				File aimfi = new File(filePathi);
				try {
					FileWriter writeri = new FileWriter(aimfi,false);
					writeri.write(jsonStri);
					System.out.println("PR "+pri.getId()+"  json file has been written !");
					writeri.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			System.out.println("No PRModelBuilder in PRModelBuilderBundle ");
		}
	}
	
	public void deleteGitSrc()
	{
		if(this.pr!=null)
		{
			this.deleteGitSrc(this.pr);
		}
		
		if(this.mdBuilderBundle!=null)
		{
			for(PRModelBuilder mdBuilderi : this.mdBuilderBundle.getPrmdlBuilders())
			{
				PullRequest pri = mdBuilderi.getPr();
				this.deleteGitSrc(pri);
			}
		}
	}
	
	public void deleteGitSrc(PullRequest pr)
	{
		if(!pr.getCommits().isEmpty())
		{
			for(Commit cmti : pr.getCommits())
			{
				if(cmti.getFlcg()!=null)
				{
				Path cmti_before = Paths.get(cmti.getFlcg().getSrcBeforeDir().getAbsolutePath());
				try {
					this.deleteDir(cmti_before);
					System.out.println("GitSrc_before delete successed !");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();	
					if(this.mdBuilder.getPrDir().exists()==false)
					{
						System.out.println("GitSrc_before not exists !");
					}else {
						System.out.println("GitSrc_before delete failed !");
					}			
				}
				Path cmti_after = Paths.get(cmti.getFlcg().getSrcAfterDir().getAbsolutePath());
				try {
					this.deleteDir(cmti_after);
					System.out.println("GitSrc_after delete successed !");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();	
					if(this.mdBuilder.getPrDir().exists()==false)
					{
						System.out.println("GitSrc_after not exists !");
					}else {
						System.out.println("GitSrc_after delete failed !");
					}			
				}
			}else {
				System.out.println("no GitSrc for "+cmti.getSha()+" needs to be deleted !");
			}
			}
		}
	}
	
	void deleteDir(Path dir)throws IOException
	{
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file , BasicFileAttributes attrs)throws IOException
			{
				Files.delete(file);
				return  FileVisitResult.CONTINUE;
			}
			@Override
			public  FileVisitResult postVisitDirectory(Path dirt ,IOException exc)throws IOException
			{
				Files.delete(dirt);
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)throws IOException
			{
				return FileVisitResult.CONTINUE;
			}
		}
		
		);		
	}
	
	


}
