package QuickAccessAPI;

import java.util.ArrayList;
import java.util.List;

import jxp3model.ChangedClassUnit;
import jxp3model.ChangedFieldUnit;
import jxp3model.ChangedFileUnit;
import jxp3model.ChangedMethodUnit;
import jxp3model.ChangedProjectUnit;
import prmodel.Commit;
import prmodel.PullRequest;

public class CodeElementRelation {
	
	PullRequest pr;
	
	CodeElementRelation(PullRequest pr)
	{
		this.pr = pr;
	}
	
	public List<ChangedClassUnit> changedClasses()
	{
		List<ChangedClassUnit> mdClasses = new ArrayList<>();
		if(!this.pr.getFlcg().getCde().getCgProjectUnits().isEmpty())
		{
			for(ChangedProjectUnit cgpji : this.pr.getFlcg().getCde().getCgProjectUnits())
			{
				if(!cgpji.getCgFileUnits().isEmpty())
				{
					for(ChangedFileUnit cgfli : cgpji.getCgFileUnits())
					{
						if(!cgfli.getCgClassUnits().isEmpty())
						{
							for(ChangedClassUnit cgclsi : cgfli.getCgClassUnits())
							{
								mdClasses.add(cgclsi);
							}
						}
					}
				}
			}
			
		}
		return mdClasses;
	}
	
	public List<ChangedClassUnit> changedClasses(String sha)
	{
		List<ChangedClassUnit> mdClasses = new ArrayList<>();
		if(!this.pr.getCommits().isEmpty())
		{
			for(Commit cmiti : this.pr.getCommits())
			{
				if(cmiti.getSha().equals(sha))
				{
					if(!cmiti.getFlcg().getCde().getCgProjectUnits().isEmpty())
					{
						for(ChangedProjectUnit cgpji : cmiti.getFlcg().getCde().getCgProjectUnits())
						{
							if(!cgpji.getCgFileUnits().isEmpty())
							{
								for(ChangedFileUnit cgfli : cgpji.getCgFileUnits())
								{
									if(!cgfli.getCgClassUnits().isEmpty())
									{
										for(ChangedClassUnit cgclsi : cgfli.getCgClassUnits())
										{
											mdClasses.add(cgclsi);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return mdClasses;
	}
	
	public List<ChangedMethodUnit> changedMethods()
	{
		List<ChangedMethodUnit> mdMethods = new ArrayList<>();
		List<ChangedClassUnit> mdClasses = this.changedClasses();
		if(!mdClasses.isEmpty())
		{
			for(ChangedClassUnit cgclsi : mdClasses)
			{
				if(!cgclsi.getCgMethodUnits().isEmpty())
				{
					for(ChangedMethodUnit cgmdi : cgclsi.getCgMethodUnits())
					{
						mdMethods.add(cgmdi);
					}
				}
			}
		}
		return mdMethods;
	}
	
	public List<ChangedMethodUnit> changedMethods(String sha)
	{
		List<ChangedMethodUnit> mdMethods = new ArrayList<>();
		List<ChangedClassUnit> mdClasses = this.changedClasses(sha);
		if(!mdClasses.isEmpty())
		{
			for(ChangedClassUnit cgclsi : mdClasses)
			{
				if(!cgclsi.getCgMethodUnits().isEmpty())
				{
					for(ChangedMethodUnit cgmdi : cgclsi.getCgMethodUnits())
					{
						mdMethods.add(cgmdi);
					}
				}
			}
		}
		return mdMethods;
	}
	
	public List<ChangedFieldUnit> changedFields()
	{
		List<ChangedFieldUnit> mdFields = new ArrayList<>();
		List<ChangedClassUnit> mdClasses = this.changedClasses();
		if(!mdClasses.isEmpty())
		{
			for(ChangedClassUnit cgclsi : mdClasses)
			{
				if(!cgclsi.getCgFieldUnits().isEmpty())
				{
					for(ChangedFieldUnit cgfdi : cgclsi.getCgFieldUnits())
					{
						mdFields.add(cgfdi);
					}
				}
			}
		}
		return mdFields;
	}
	
	public List<ChangedFieldUnit> changedFields(String sha)
	{
		List<ChangedFieldUnit> mdFields = new ArrayList<>();
		List<ChangedClassUnit> mdClasses = this.changedClasses(sha);
		if(!mdClasses.isEmpty())
		{
			for(ChangedClassUnit cgclsi : mdClasses)
			{
				if(!cgclsi.getCgFieldUnits().isEmpty())
				{
					for(ChangedFieldUnit cgfdi : cgclsi.getCgFieldUnits())
					{
						mdFields.add(cgfdi);
					}
				}
			}
		}
		return mdFields;
	}

}
